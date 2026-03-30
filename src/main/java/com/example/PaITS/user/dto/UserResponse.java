package com.example.PaITS.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@Getter
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private boolean isActive;
    private String avatarUrl;
}

