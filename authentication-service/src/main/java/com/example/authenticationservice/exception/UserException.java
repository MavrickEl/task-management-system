package com.example.authenticationservice.exception;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(String.format(message));
    }
}
