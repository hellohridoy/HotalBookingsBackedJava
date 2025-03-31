package com.example.HotelFivePlusBackedJava.service.interfaces;

import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.entity.Bookings;

public interface IBookingsService {

    Response findBookingsByConfirmationCode(String confirmationCode);

    Response saveBooking(Long roomId, Long userId, Bookings bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingsId);


}
