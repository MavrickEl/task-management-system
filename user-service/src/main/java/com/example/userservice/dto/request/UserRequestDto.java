package com.example.userservice.dto.request;

import com.example.userservice.enums.Role;

public record UserRequestDto(
        String name,
        String secondName,
        String email,
        String password,
        Role role
) {
}
