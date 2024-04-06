package com.example.userservice.mapper.Impl;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;

public class UserMapperImpl implements UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder().id(user.getId()).name(user.getName()).secondName(user.getSecondName()).build();
    }

    public User toUser(UserRequest userRequest) {
        return User.builder().name(userRequest.getName()).secondName(userRequest.getSecondName())
                .email(userRequest.getEmail()).password(userRequest.getPassword()).role(userRequest.getRole()).build();
    }
}
