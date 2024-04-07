package com.example.authenticationservice.service;

import com.example.authenticationservice.exception.AuthenticationException;
import com.example.authenticationservice.model.AuthRequest;
import com.example.authenticationservice.model.AuthResponse;
import com.example.authenticationservice.model.User;
import com.example.authenticationservice.util.PasswordUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        request.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        User registeredUser = restTemplate.postForObject("http://user-service/api/users", request, User.class);

        String accessToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse signin(AuthRequest request) {
        User user = restTemplate.postForObject("http://user-service/api/users/login", request, User.class);
        if (authenticateUser(request.getPassword(), user.getPassword())) {
            String accessToken = jwtUtil.generate(user.getId(), user.getRole(), "ACCESS");
            String refreshToken = jwtUtil.generate(user.getId(), user.getRole(), "REFRESH");
            return new AuthResponse(accessToken, refreshToken);
        } else {
            throw new AuthenticationException("Неверное имя пользователя или пароль");
        }
    }

    private boolean authenticateUser(String password, String requestPassword) {
        return PasswordUtil.checkPassword(password, requestPassword);
    }
}
