package com.example.HotelFivePlusBackedJava.service.interfaces;

import com.example.HotelFivePlusBackedJava.dto.LoginRequest;
import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.entity.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response deleteUser(Long userId);

    Response getUserBookingsHistory(Long userId);

    Response getMyInfos(String email);

    Response getUserById(Long userId);


}
