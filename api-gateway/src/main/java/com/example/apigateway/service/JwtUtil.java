package com.example.apigateway.service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final JWTVerifier verifier;
    private static final String EMPTY_TOKEN = "Empty jwt token";

    public void validateTokenFromHeaders(HttpHeaders headers) {
        String token = getAuthorizationToken(headers);
        verifyToken(token);
    }

    public void verifyToken(String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        verifier.verify(token);
    }

    public String getAuthorizationToken(HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            throw new JWTVerificationException(EMPTY_TOKEN);
        }
        return token;
    }
}