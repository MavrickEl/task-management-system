package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String secondName;
//    private String email;
//    private String password;
//    private Role role;
}
