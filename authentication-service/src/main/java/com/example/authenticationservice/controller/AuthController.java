package com.example.authenticationservice.controller;

import com.example.authenticationservice.model.AuthRequest;
import com.example.authenticationservice.model.AuthResponse;
import com.example.authenticationservice.service.Impl.AuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auths/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping(value = "/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping(value = "/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}