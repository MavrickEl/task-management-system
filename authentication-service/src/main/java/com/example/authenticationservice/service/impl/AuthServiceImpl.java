package com.example.authenticationservice.service.impl;

import com.example.authenticationservice.dto.request.AuthenticationRequestDto;
import com.example.authenticationservice.dto.request.UserRequestDto;
import com.example.authenticationservice.dto.response.AuthResponseDto;
import com.example.authenticationservice.exception.AuthenticationException;
import com.example.authenticationservice.exception.UserException;
import com.example.authenticationservice.model.User;
import com.example.authenticationservice.service.AuthService;
import com.example.authenticationservice.util.JwtUtil;
import com.example.authenticationservice.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final WebClient webClient;
    private final JwtUtil jwtUtil;
    private static final String DETAIL_USER_URI = "/users/api/user";

    @Override
    public AuthResponseDto register(UserRequestDto request) {
        request.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        String exception = "уже зарегистрирован";
        User user = postUser("", exception, request);
        if (user == null) {
            throw new UserException("Не удалось создать пользователя");
        }
        return getAuthResponse(user);
    }

    @Override
    public AuthResponseDto signin(AuthenticationRequestDto request) {
        String exception = "не найден";
        User user = postUser("/login", exception, request);
        if (user == null) {
            throw new UserException("Пользователь с email " + request.email() + " не найден");
        }
        if (authenticateUser(request.password(), user.getPassword())) {
            return getAuthResponse(user);
        } else {
            throw new AuthenticationException("Неверное имя пользователя или пароль");
        }
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken = authHeader.replace("Bearer ", "");
        final String userId = jwtUtil.extractId(refreshToken);
        if (userId != null) {
            User user = webClient
                    .get()
                    .uri(DETAIL_USER_URI + "/" + userId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new UserException("Пользователь не найден")))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new UserException("Внутренняя ошибка сервера")))
                    .bodyToMono(User.class)
                    .block();
            new ObjectMapper().writeValue(response.getOutputStream(), getAuthResponse(user));
        }
    }

    private User postUser(String url, String exceptionMessage, Object request) {
        return webClient
                .post()
                .uri(DETAIL_USER_URI + url)
                .body(Mono.just(request), Object.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new UserException("Пользователь " + exceptionMessage)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new UserException("Внутренняя ошибка сервера")))
                .bodyToMono(User.class)
                .block();
    }

    private boolean authenticateUser(String password, String requestPassword) {
        return PasswordUtil.checkPassword(password, requestPassword);
    }

    private AuthResponseDto getAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccess(user);
        String newRefreshToken = jwtUtil.generateRefresh(user);
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
