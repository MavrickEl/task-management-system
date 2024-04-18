package com.example.authenticationservice.dto.response;

public record AuthResponseDto(
        String accessToken,
        String refreshToken
) {
}