package com.example.authenticationservice.exception.handler;

import com.example.authenticationservice.dto.response.ErrorResponseDto;
import com.example.authenticationservice.exception.AuthenticationException;
import com.example.authenticationservice.exception.UserException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> wrongValidation(Exception ex) {
        String errorMessage = String.format("Wrong validation: %s", ex.getMessage());
        return ResponseEntity.status(400).body(new ErrorResponseDto(errorMessage));
    }

    @ExceptionHandler(value = {
            AuthenticationException.class,
            UserException.class,
            IOException.class
    })
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleInternalServerError(Exception ex) {
        String errorMessage = String.format("Server error: %s", ex.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponseDto(errorMessage));
    }
}