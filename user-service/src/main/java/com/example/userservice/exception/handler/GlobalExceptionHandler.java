package com.example.userservice.exception.handler;

import com.example.userservice.dto.response.ErrorResponseDto;
import com.example.userservice.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleInternalServerError(Exception ex) {
        String errorMessage = String.format("Server error: %s", ex.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponseDto(errorMessage));
    }
}