package com.example.HotelFivePlusBackedJava.controller;

import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.entity.User;
import com.example.HotelFivePlusBackedJava.repository.UserRepository;
import com.example.HotelFivePlusBackedJava.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final IUserService iUserService;

    @GetMapping("/api/users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        Response response = iUserService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/api/get-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable Long userId) {
        Response response = iUserService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/api/delete/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long userId) {
        Response response = iUserService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/api/current-user-profile")
    public ResponseEntity<Response> currentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Response response = new Response();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/api/user-bookings")
    public ResponseEntity<Response> getUserBookingsHistory(@PathVariable("userId")String userId) {
        Response response = new Response();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
