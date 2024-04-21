package com.example.userservice.exception.handler;

import com.example.userservice.dto.response.ErrorResponseDto;
import com.example.userservice.exception.UserException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> wrongValidation(Exception ex) {
        String errorMessage = String.format("Wrong validation: %s", ex.getMessage());
        return ResponseEntity.status(400).body(new ErrorResponseDto(errorMessage));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleInternalServerError(Exception ex) {
        String errorMessage = String.format("Server error: %s", ex.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponseDto(errorMessage));
    }
}