package com.example.authenticationservice.util;

import com.example.authenticationservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshExpiration;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccess(User user) {
        Map<String, String> claims = Map.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "secondName", user.getSecondName());
        return buildToken(claims, user, expiration);
    }

    public String generateRefresh(User user) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    private String buildToken(Map<String, String> claims, User user, long expiration) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getId())
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractId(String token) {
        Claims claims = (Claims) Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parse(token)
                .getBody();

        return claims.getSubject();
    }
}
