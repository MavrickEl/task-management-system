package com.example.userservice.mapper;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.User;

public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUser(UserRequest userRequest);
}
