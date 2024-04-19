package com.example.userservice.mapper.impl;

import com.example.userservice.dto.request.UserFieldsRequestDto;
import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.enums.Role;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;

public class UserMapperImpl implements UserMapper {
    public UserResponseDto toUserResponse(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getSecondName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    public User toUser(UserRequestDto userRequest) {
        return User.builder().name(userRequest.name()).secondName(userRequest.secondName())
                .email(userRequest.email()).password(userRequest.password()).role(Role.valueOf(userRequest.role())).build();
    }

    public User toUser(UserFieldsRequestDto userRequest) {
        return User.builder().name(userRequest.name()).secondName(userRequest.secondName()).role(Role.valueOf(userRequest.role())).build();
    }
}
