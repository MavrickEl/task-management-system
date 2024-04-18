package com.example.authenticationservice.controller;

import com.example.authenticationservice.dto.request.AuthenticationRequestDto;
import com.example.authenticationservice.dto.request.UserRequestDto;
import com.example.authenticationservice.dto.response.AuthResponseDto;
import com.example.authenticationservice.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auths/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signup")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(message);
        }
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<AuthResponseDto> signin(@Valid @RequestBody AuthenticationRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(message);
        }
        return ResponseEntity.ok(authService.signin(request));
    }
}