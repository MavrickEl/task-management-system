package com.example.authenticationservice.exception;

public class UserException extends RuntimeException {
    private static final String REQ_FAILED = "%s failed with status %s\nuri: %s";

    public UserException(String method, String uri, String status) {
        super(String.format(REQ_FAILED, method, status, uri));
    }
}
