package com.example.userservice.service.Impl;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
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
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserException("Пользователь с ID " + id + " не найден"));
        return mapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(mapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new UserException("Пользователь с email " + userRequest.getEmail() + " уже существует");
        }
        return mapper.toUserResponse(userRepository.save(mapper.toUser(userRequest)));
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("Пользователь с ID " + id + " не найден"));

        user.setName(userRequest.getName());
        user.setSecondName(userRequest.getSecondName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());

        return mapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("Пользователь с ID " + id + " не найден"));
        userRepository.delete(user);
    }
}
