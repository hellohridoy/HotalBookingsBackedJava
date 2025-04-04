package com.example.HotelFivePlusBackedJava.controller;

import com.example.HotelFivePlusBackedJava.dto.LoginRequest;
import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.entity.User;
import com.example.HotelFivePlusBackedJava.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
