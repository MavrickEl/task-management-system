package com.example.authenticationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String name;
    private String secondName;
    private String email;
    private String password;
}