package com.example.userservice.service.Impl;

import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.exception.UserException;
import com.example.userservice.mapper.Impl.UserMapperImpl;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepo;
import com.example.userservice.service.UserService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapperImpl mapper;
    private final UserRepo userRepository;

    public UserServiceImpl(UserRepo userRepository) {
        this.mapper = new UserMapperImpl();
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = getUser(id);
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
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new UserException("Пользователь с email " + userRequest.email() + " уже существует");
        }
        return mapper.toUserResponse(userRepository.save(mapper.toUser(userRequest)));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto userRequest) {
        User user = getUser(id);
        user.setName(userRequest.name());
        user.setSecondName(userRequest.secondName());
        user.setRole(userRequest.role());
        return mapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDto partialUpdate(Long id, UserRequestDto userRequest) throws IllegalAccessException {
        User user = getUser(id);
        updateUser(user, mapper.toUser(userRequest));
        return mapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
    }


    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("Пользователь с ID " + id + " не найден"));
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
