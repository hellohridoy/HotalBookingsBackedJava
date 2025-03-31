package com.example.HotelFivePlusBackedJava.dto;

import com.example.HotelFivePlusBackedJava.entity.Room;
import com.example.HotelFivePlusBackedJava.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingsDTO {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int totalNumberOfAdult;

    private int totalNumberOfChildren;

    private int totalNumberOfGuest;

    private String bookingConfirmationCode;

    private UserDTO userDTO;

    private RoomDTO roomDTO;

}
