package com.example.authenticationservice.service.Impl;

import com.example.authenticationservice.exception.AuthenticationException;
import com.example.authenticationservice.exception.UserException;
import com.example.authenticationservice.model.AuthRequest;
import com.example.authenticationservice.model.AuthResponse;
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

    public AuthResponse register(AuthRequest request) {
        request.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        User user = webClient
                .post()
                .uri(GET_DETAIL_URI)
                .body(Mono.just(request), AuthRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new UserException("POST", GET_DETAIL_URI, clientResponse.statusCode().toString())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new UserException("POST", GET_DETAIL_URI, clientResponse.statusCode().toString())))
                .bodyToMono(User.class)
                .block();
        String accessToken = jwtUtil.generate(user, "ACCESS");
        String refreshToken = jwtUtil.generate(user, "REFRESH");
        return new AuthResponse(accessToken, refreshToken);

    }

    public AuthResponse signin(AuthRequest request) {
        User user = webClient
                .post()
                .uri(GET_DETAIL_URI + "/login")
                .body(Mono.just(request), AuthRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new UserException("POST", GET_DETAIL_URI, clientResponse.statusCode().toString())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new UserException("POST", GET_DETAIL_URI, clientResponse.statusCode().toString())))
                .bodyToMono(User.class)
                .block();
        if (authenticateUser(request.getPassword(), user.getPassword())) {
            String accessToken = jwtUtil.generate(user, "ACCESS");
            String refreshToken = jwtUtil.generate(user, "REFRESH");
            return new AuthResponse(accessToken, refreshToken);
        } else {
            throw new AuthenticationException("Неверное имя пользователя или пароль");
        }
    }

    private boolean authenticateUser(String password, String requestPassword) {
        return PasswordUtil.checkPassword(password, requestPassword);
    }
}
