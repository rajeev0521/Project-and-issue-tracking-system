package com.example.PaITS.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;


}