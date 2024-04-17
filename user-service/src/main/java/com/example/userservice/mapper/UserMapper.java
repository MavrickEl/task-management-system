package com.example.userservice.mapper;

import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.model.User;

public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toUser(UserRequestDto userRequest);
}
