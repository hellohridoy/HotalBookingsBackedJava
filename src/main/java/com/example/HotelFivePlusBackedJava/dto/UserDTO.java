package com.example.HotelFivePlusBackedJava.dto;

import com.example.HotelFivePlusBackedJava.entity.Bookings;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private String role;
    private List<BookingsDTO>bookings = new ArrayList<>();
}
