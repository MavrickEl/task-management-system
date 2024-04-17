package com.example.userservice.util;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenDecoder {

    private final JWTVerifier verifier;
    private static final String EMPTY_TOKEN = "Empty token";

    public void validateToken(HttpHeaders headers) {
        String token = getAuthorizationToken(headers);
        verifyToken(token);
    }

    public void verifyToken(String bearerToken) {
        String[] splitToken = bearerToken.split("\\s");
        String token;
        if (splitToken.length > 1) {
            token = splitToken[1];
        } else {
            token = splitToken[0];
        }
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
