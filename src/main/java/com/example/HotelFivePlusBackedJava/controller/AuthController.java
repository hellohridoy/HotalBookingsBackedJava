package com.example.HotelFivePlusBackedJava.controller;

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

    private IUserService userService;

    @PostMapping("/api/auth/register")
    private ResponseEntity<Response>register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/api/auth/login")
    private ResponseEntity<Response>login(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
