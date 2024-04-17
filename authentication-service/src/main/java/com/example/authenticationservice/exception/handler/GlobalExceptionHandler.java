package com.example.authenticationservice.exception.handler;

import com.example.authenticationservice.dto.ErrorResponseDto;
import com.example.authenticationservice.exception.UserException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            AuthenticationException.class,
            UserException.class
    })
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleInternalServerError(Exception ex) {
        String errorMessage = String.format("Server error: %s", ex.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponseDto(errorMessage));
    }
}