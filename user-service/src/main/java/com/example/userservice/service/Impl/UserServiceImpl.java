package com.example.userservice.service.Impl;

import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.UserException;
import com.example.userservice.mapper.Impl.UserMapperImpl;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepo;
import com.example.userservice.service.UserService;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserException("Пользователь с ID " + id + " не найден"));
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("Пользователь с ID " + id + " не найден"));

        user.setName(userRequest.name());
        user.setSecondName(userRequest.secondName());
        user.setRole(userRequest.role());

        return mapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("Пользователь с ID " + id + " не найден"));
        userRepository.delete(user);
    }
}
