package com.example.userservice.provider;

import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.enums.Role;

public class UserResponseDtoProvider {
    public static UserResponseDto getUserResponse() {
        return new UserResponseDto(1L, "Jim", "Jones", "Jim.Jones@example.com", "password", Role.USER);
    }

    public static UserResponseDto getUpdateUserResponse() {
        return new UserResponseDto(1L, "Jon", "Jes", "Jon.Jes@example.com", "updatePassword", Role.ADMIN);
    }
    public static UserResponseDto getUpdateFieldsUserResponse() {
        return new UserResponseDto(1L, "Jon", "Jes", "Jim.Jones@example.com", "password", Role.ADMIN);
    }
}
