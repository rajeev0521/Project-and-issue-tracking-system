package com.example.PaITS.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequest {

    private String fullName;
    private String username;
    private String password;
    private String avatarUrl;
}
