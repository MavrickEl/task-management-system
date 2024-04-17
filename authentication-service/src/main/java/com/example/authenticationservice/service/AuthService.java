package com.example.authenticationservice.service;

import com.example.authenticationservice.model.AuthRequest;
import com.example.authenticationservice.model.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request);
    AuthResponse signin(AuthRequest request);
}
