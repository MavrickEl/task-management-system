package com.example.userservice.dto.response;

import com.example.userservice.enums.Role;


public record UserResponseDto(
        Long id,
        String name,
        String secondName,
        String email,
        String password,
        Role role
) {
}
