package com.example.HotelFivePlusBackedJava.service.impl;

import com.example.HotelFivePlusBackedJava.dto.LoginRequest;
import com.example.HotelFivePlusBackedJava.dto.Response;
import com.example.HotelFivePlusBackedJava.dto.UserDTO;
import com.example.HotelFivePlusBackedJava.entity.User;
import com.example.HotelFivePlusBackedJava.exception.MyExceptions;
import com.example.HotelFivePlusBackedJava.repository.UserRepository;
import com.example.HotelFivePlusBackedJava.service.interfaces.IUserService;
import com.example.HotelFivePlusBackedJava.utils.JWTUtils;
import com.example.HotelFivePlusBackedJava.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if (user.getRole()==null || user.getRole().isBlank()) {
                user.setRole("ROLE_USER");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new MyExceptions(user.getEmail()+"Email is already in use");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);

        }catch (MyExceptions exception){
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getUsername()).orElseThrow(() -> new MyExceptions("User not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successfully logged in");
        }catch (MyExceptions exception){
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
       return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> user = userRepository.findAll();
            List<UserDTO>userDTOList = Utils.mapUserListEntityToUserListDTO(user);
            response.setStatusCode(200);
            response.setMessage(userDTOList.toString());
            response.setUserList(userDTOList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users"+e.getMessage());
        }
        return response;
    }



    @Override
    public Response deleteUser(Long userId) {
        Response response = new Response();
        try {
            userRepository.findById(userId).orElseThrow(() -> new MyExceptions("User not found"));
            userRepository.deleteById(userId);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted user");
        }catch (MyExceptions exception){
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingsHistory(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new MyExceptions("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage(userDTO.toString());
            response.setUser(userDTO);
        }catch (MyExceptions exception){
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfos(String email) {
        Response response = new Response();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new MyExceptions("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successfully");
            response.setUser(userDTO);

        }catch (MyExceptions exception){
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new MyExceptions("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("User found successfully");
            response.setUser(userDTO);
        } catch (MyExceptions exception) {
            response.setStatusCode(400);
            response.setMessage(exception.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving user: " + e.getMessage());
        }
        return response;
    }
}
