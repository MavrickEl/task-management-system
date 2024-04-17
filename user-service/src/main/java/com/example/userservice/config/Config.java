package com.example.userservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JWTVerifier getJWTVerifier() {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("management-system")
                .build();
    }
}
