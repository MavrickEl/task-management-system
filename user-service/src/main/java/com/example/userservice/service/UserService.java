package com.example.userservice.service;

import com.example.userservice.dto.request.UserFieldsRequestDto;
import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getById(Long id);

    UserResponseDto getByEmail(String email);

    List<UserResponseDto> getAll();

    UserResponseDto save(UserRequestDto userRequest);

    UserResponseDto update(Long id, UserRequestDto userRequest);

    UserResponseDto updateFields(Long id, UserFieldsRequestDto userRequest) throws IllegalAccessException;

    void deleteById(Long id);
}
