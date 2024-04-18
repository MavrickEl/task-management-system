package com.example.apigateway.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.apigateway.service.JwtUtil;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    private final RouterValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouterValidator validator, JwtUtil jwtUtil) {
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (validator.isSecured.test(request)) {
            try {
                jwtUtil.validateTokenFromHeaders(request.getHeaders());
            } catch (JWTVerificationException e) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String message) {
        DataBuffer db = new DefaultDataBufferFactory().wrap(message.getBytes());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.writeWith(Mono.just(db));
    }
}
