package com.example.HotelFivePlusBackedJava.service.interfaces;

import com.example.HotelFivePlusBackedJava.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {

    Response addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice, String description) throws IOException;

    Response getAllRoomType(String roomType);

    Response getAllRooms(); // Changed from List<String>

    Response getRoomById(Long roomId);

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, MultipartFile file, String roomType, BigDecimal roomPrice, String description);

    Response getAllAvailableRoom();

    Response getAllAvailableRoomsByDateAndRoomType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
