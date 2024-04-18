package com.example.userservice.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserFieldsRequestDto(
        @Size(min = 1, max = 255, message = "name must be between 1 and 255 characters")
        String name,

        @Size(min = 1, max = 255, message = "secondName must be between 1 and 255 characters")
        String secondName,

        @Pattern(message = "Invalid Enum role name", regexp = "^(USER|ADMIN)$")
        String role
) {
}
