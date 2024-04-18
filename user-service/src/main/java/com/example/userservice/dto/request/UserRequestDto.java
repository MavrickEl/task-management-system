package com.example.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserRequestDto(
        @NotBlank(message = "name must not be empty")
        @NotNull(message = "name must not be null")
        @Size(max = 255, message = "name must be between 1 and 255 characters")
        String name,
        @NotBlank(message = "secondName must not be empty")
        @NotNull(message = "secondName must not be null")
        @Size(max = 255, message = "secondName must be between 1 and 255 characters")
        String secondName,
        @NotBlank(message = "email must not be empty")
        @Email(message = "invalid email format, example: test@mail.com")
        @NotNull(message = "email must not be null")
        String email,
        @NotBlank(message = "password must not be empty")
        @NotNull(message = "password must not be null")
        String password,
        @NotBlank(message = "role must not be empty")
        @NotNull(message = "role must not be null")
        @Pattern(message = "Invalid Enum role name", regexp = "^(USER|ADMIN)$")
        String role
) {
}
