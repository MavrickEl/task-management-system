package com.example.authenticationservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record AuthenticationRequestDto(
        @NotBlank(message = "email must not be empty")
        @Email(message = "invalid email format, example: email@mail.com")
        @NotNull(message = "email must not be null")
        String email,
        @NotBlank(message = "password must not be empty")
        @NotNull(message = "password must not be null")
        String password
) {
}
