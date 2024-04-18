package com.example.authenticationservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@Validated
public class UserRequestDto {
    @NotBlank(message = "name must not be empty")
    @NotNull(message = "name must not be null")
    @Size(max = 255, message = "name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "secondName must not be empty")
    @NotNull(message = "secondName must not be null")
    @Size(max = 255, message = "secondName must be between 1 and 255 characters")
    private String secondName;

    @NotBlank(message = "email must not be empty")
    @Email(message = "invalid email format, example: test@mail.com")
    @NotNull(message = "email must not be null")
    private String email;

    @NotBlank(message = "password must not be empty")
    @NotNull(message = "password must not be null")
    private String password;

    @NotBlank(message = "role must not be empty")
    @NotNull(message = "role must not be null")
    @Pattern(message = "Invalid Enum role name", regexp = "^(USER|ADMIN)$")
    private String role;
}