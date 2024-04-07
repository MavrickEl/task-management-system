package com.example.userservice.dto;

import com.example.userservice.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String secondName;
    private String email;
    private String password;
    private Role role;
}
