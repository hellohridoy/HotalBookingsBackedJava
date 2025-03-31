package com.example.HotelFivePlusBackedJava.service.impl;

import com.example.HotelFivePlusBackedJava.dto.BookingsDTO;
import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.entity.Bookings;
import com.example.HotelFivePlusBackedJava.entity.Room;
import com.example.HotelFivePlusBackedJava.entity.User;
import com.example.HotelFivePlusBackedJava.exception.MyExceptions;
import com.example.HotelFivePlusBackedJava.repository.BookingsRepository;
import com.example.HotelFivePlusBackedJava.repository.RoomRepository;
import com.example.HotelFivePlusBackedJava.repository.UserRepository;
import com.example.HotelFivePlusBackedJava.service.interfaces.IBookingsService;
import com.example.HotelFivePlusBackedJava.service.interfaces.IRoomService;
import com.example.HotelFivePlusBackedJava.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingsServiceImpl implements IBookingsService {

    private final BookingsRepository bookingRepository;
    private final  RoomRepository roomRepository;
    private final  UserRepository userRepository;

    @Override
    public Response findBookingsByConfirmationCode(String confirmationCode) {

        Response response = new Response();

        try {
            Bookings booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new MyExceptions("Booking Not Found"));
            BookingsDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingsDTO(bookingDTO);

        } catch (MatchException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response saveBooking(Long roomId, Long userId, Bookings bookingRequest) {
        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyExceptions(""));
            User user = userRepository.findById(userId).orElseThrow(() -> new MyExceptions("User Not Found"));

            List<Bookings> existingBookings = room.getBookings();

            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new MyExceptions("Room not Available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (MyExceptions e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking: " + e.getMessage());

        }
        return response;
    }


    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {

        Response response = new Response();

        try {
            Bookings booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new MyExceptions("Booking Not Found"));
            BookingsDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingsDTO(bookingDTO);

        } catch (MyExceptions e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();

        try {
            List<Bookings> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingsDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingsList(bookingDTOList);

        } catch (MyExceptions e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting all bookings: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {

        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new MyExceptions("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (MatchException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking: " + e.getMessage());

        }
        return response;
    }


    private boolean roomIsAvailable(Bookings bookingRequest, List<Bookings> existingBookings) {

        return existingBookings.stream()
            .noneMatch(existingBooking ->
                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                    || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                    || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                    && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                    || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                    && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                    || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                    && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                    || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                    && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                    || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                    && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
            );
    }
}
