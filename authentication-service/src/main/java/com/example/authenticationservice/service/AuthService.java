package com.example.authenticationservice.service;

import com.example.authenticationservice.dto.request.AuthenticationRequestDto;
import com.example.authenticationservice.dto.request.UserRequestDto;
import com.example.authenticationservice.dto.response.AuthResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthResponseDto register(UserRequestDto request);
    AuthResponseDto signin(AuthenticationRequestDto request);

    void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
