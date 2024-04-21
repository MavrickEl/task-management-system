package com.example.authenticationservice.dto.response;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        String accessToken,
        String refreshToken
) {
}