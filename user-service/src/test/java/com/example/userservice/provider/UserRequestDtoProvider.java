package com.example.userservice.provider;

import com.example.userservice.dto.request.UserRequestDto;

public class UserRequestDtoProvider {
    public static UserRequestDto getSaveUserRequest() {
        return new UserRequestDto("Jim", "Jones", "Jim.Jones@example.com", "password", "USER");
    }

    public static UserRequestDto getUpdateUserRequest() {
        return new UserRequestDto("Jon", "Jes", "Jon.Jes@example.com", "updatePassword", "ADMIN");
    }
}
