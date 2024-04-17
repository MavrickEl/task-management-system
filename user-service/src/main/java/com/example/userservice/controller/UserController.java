package com.example.userservice.controller;

import com.example.userservice.dto.request.UserRequestDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.service.UserService;
import com.example.userservice.util.TokenDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TokenDecoder tokenDecoder;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsers(@RequestHeader HttpHeaders headers) {
        tokenDecoder.validateToken(headers);
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        tokenDecoder.validateToken(headers);
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto user) {
        return ResponseEntity.ok(userService.getByEmail(user.email()));
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@RequestHeader HttpHeaders headers, @PathVariable Long id, @RequestBody UserRequestDto user) {
        tokenDecoder.validateToken(headers);
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        tokenDecoder.validateToken(headers);
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
