package com.example.authenticationservice.service.Impl;

import com.example.authenticationservice.dto.request.AuthenticationRequestDto;
import com.example.authenticationservice.dto.request.UserRequestDto;
import com.example.authenticationservice.dto.response.AuthResponseDto;
import com.example.authenticationservice.exception.AuthenticationException;
import com.example.authenticationservice.exception.UserException;
import com.example.authenticationservice.model.User;
import com.example.authenticationservice.service.AuthService;
import com.example.authenticationservice.util.JwtUtil;
import com.example.authenticationservice.util.PasswordUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final WebClient webClient;
    private final JwtUtil jwtUtil;
    private static final String GET_DETAIL_URI = "/users/api/user";

    public AuthResponseDto register(UserRequestDto request) {
        request.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        User user = webClient
                .post()
                .uri(GET_DETAIL_URI)
                .body(Mono.just(request), UserRequestDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new UserException("Пользователь с email " + request.getEmail() + " уже зарегистрирован")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new UserException("Внутренняя ошибка сервера")))
                .bodyToMono(User.class)
                .block();
        if (user == null) {
            throw new UserException("Не удалось создать пользователя");
        }
        String accessToken = jwtUtil.generate(user, "ACCESS");
        String refreshToken = jwtUtil.generate(user, "REFRESH");
        return new AuthResponseDto(accessToken, refreshToken);
    }

    public AuthResponseDto signin(AuthenticationRequestDto request) {
        User user = webClient
                .post()
                .uri(GET_DETAIL_URI + "/login")
                .body(Mono.just(request), UserRequestDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new UserException("Пользователь с email " + request.email() + " не найден")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new UserException("Внутренняя ошибка сервера")))
                .bodyToMono(User.class)
                .block();
        if (user == null) {
            throw new UserException("Пользователь с email " + request.email() + " не найден");
        }
        if (authenticateUser(request.password(), user.getPassword())) {
            String accessToken = jwtUtil.generate(user, "ACCESS");
            String refreshToken = jwtUtil.generate(user, "REFRESH");
            return new AuthResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthenticationException("Неверное имя пользователя или пароль");
        }
    }

    private boolean authenticateUser(String password, String requestPassword) {
        return PasswordUtil.checkPassword(password, requestPassword);
    }
}
