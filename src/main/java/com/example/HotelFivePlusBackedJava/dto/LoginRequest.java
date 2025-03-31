package com.example.HotelFivePlusBackedJava.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {
    @NotBlank(message = "username is required or unique")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
}
