package com.example.HotelFivePlusBackedJava.service.impl;

import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.dto.RoomDTO;
import com.example.HotelFivePlusBackedJava.entity.Room;
import com.example.HotelFivePlusBackedJava.exception.MyExceptions;
import com.example.HotelFivePlusBackedJava.repository.RoomRepository;
import com.example.HotelFivePlusBackedJava.service.interfaces.IRoomService;
import com.example.HotelFivePlusBackedJava.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;
    private final String uploadDir = "uploads/rooms/";

    @Override
    public Response addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();
        try {
            // Validate inputs
            if (file == null || file.isEmpty()) {
                throw new MyExceptions("Please select an image file to upload");
            }
            if (roomType == null || roomType.trim().isEmpty()) {
                throw new MyExceptions("Room type is required");
            }
            if (roomPrice == null || roomPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new MyExceptions("Room price must be greater than zero");
            }

            // Check file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new MyExceptions("Only image files are allowed");
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFileName = UUID.randomUUID() + fileExtension;

            // Define upload path
            Path uploadPath = Paths.get(uploadDir);

            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create and save the room
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            room.setRoomPhotoUrl(uploadDir + newFileName);

            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);

            response.setStatusCode(200);
            response.setMessage("Room added successfully");
            response.setRoom(roomDTO);

        } catch (MyExceptions exception) {
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding new room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllRoomType(String roomType) {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findByRoomType(roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(rooms);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved rooms of type: " + roomType);
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting rooms by type: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findAll();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(rooms);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved all rooms");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyExceptions("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(room);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved room");
            response.setRoom(roomDTO);

        } catch (MyExceptions exception) {
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting room by ID: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyExceptions("Room not found"));

            // You might want to check if the room has active bookings before deletion
            // and handle the file deletion logic for the image

            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted room");

        } catch (MyExceptions exception) {
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, MultipartFile file, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyExceptions("Room not found"));

            // Update room data
            if (roomType != null && !roomType.trim().isEmpty()) {
                room.setRoomType(roomType);
            }

            if (roomPrice != null && roomPrice.compareTo(BigDecimal.ZERO) > 0) {
                room.setRoomPrice(roomPrice);
            }

            if (description != null) {
                room.setRoomDescription(description);
            }

            // Handle file update if provided
            if (file != null && !file.isEmpty()) {
                // Check file type
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new MyExceptions("Only image files are allowed");
                }

                // Generate unique filename
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                String newFileName = UUID.randomUUID() + fileExtension;

                // Define upload path
                Path uploadPath = Paths.get(uploadDir);

                // Create directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                Path filePath = uploadPath.resolve(newFileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Update image URL
                room.setRoomPhotoUrl(uploadDir + newFileName);

                // TODO: Delete old image file if needed
            }

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setRoom(roomDTO);

        } catch (MyExceptions exception) {
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRoom() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findByAvailableTrue();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(rooms);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved all available rooms");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRoomsByDateAndRoomType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
        try {
            // This would require a custom query in your repository
            // that checks for rooms of the specified type that don't have bookings
            // overlapping with the given date range
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndType(
                checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatusCode(200);
            response.setMessage("Successfully retrieved available rooms for the selected dates and type");
            response.setRoomList(roomDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms by date and type: " + e.getMessage());
        }
        return response;
    }
}
