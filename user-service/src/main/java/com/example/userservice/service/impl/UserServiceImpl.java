package com.example.userservice.service.impl;

import com.example.userservice.dto.request.UserFieldsRequestDto;
import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.UserException;
import com.example.userservice.mapper.impl.UserMapperImpl;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepo;
import com.example.userservice.service.UserService;
import com.example.userservice.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepo userRepository;

    public UserServiceImpl(UserRepo userRepository) {
        this.mapper = new UserMapperImpl();
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = getUserById(id);
        return mapper.toUserResponse(user);
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserException("Пользователь с email " + email + " не найден"));
        return mapper.toUserResponse(user);
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(mapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequest) {
        checkExistUserByEmail(userRequest.email());
        return mapper.toUserResponse(userRepository.save(mapper.toUser(userRequest)));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto userRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userRequest.name());
                    user.setSecondName(userRequest.secondName());
                    checkExistUserByEmail(userRequest.email());
                    if (!userRequest.email().equals(user.getEmail())) {
                        checkExistUserByEmail(userRequest.email());
                    }
                    user.setEmail(userRequest.email());
                    user.setPassword(PasswordUtil.hashPassword(userRequest.password()));
                    user.setRole(Role.valueOf(userRequest.role()));
                    return mapper.toUserResponse(userRepository.save(user));
                })
                .orElseGet(() -> {
                    checkExistUserByEmail(userRequest.email());
                    User user = mapper.toUser(userRequest);
                    user.setPassword(PasswordUtil.hashPassword(userRequest.password()));
                    return mapper.toUserResponse(userRepository.save(user));
                });
    }

    @Override
    public UserResponseDto updateFields(Long id, UserFieldsRequestDto userRequest) throws IllegalAccessException {
        User user = getUserById(id);
        updateUser(user, mapper.toUser(userRequest));
        return mapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("Пользователь с ID " + id + " не найден"));
    }

    private void checkExistUserByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("Пользователь с email " + email + " уже существует");
        }
    }

    private void updateUser(User existingUser, User requestUser) throws IllegalAccessException {
        Class<?> internClass = User.class;
        Field[] internFields = internClass.getDeclaredFields();
        for (Field field : internFields) {
            field.setAccessible(true);
            Object value = field.get(requestUser);
            if (value != null) {
                field.set(existingUser, value);
            }
            field.setAccessible(false);
        }

    }
}
