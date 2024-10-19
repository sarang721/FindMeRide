package com.example.uberapp.UberApp.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.uberapp.UberApp.dto.DriverDto;
import com.example.uberapp.UberApp.dto.SignupDto;
import com.example.uberapp.UberApp.dto.UserDto;

@Service
public interface AuthService {
    String[] login(String email, String password);
    UserDto signUp(SignupDto signupDto);
    DriverDto onboardNewDriver(Long userId, String vehicleId);
    String refreshToken(String refreshToken);
}
