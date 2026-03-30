package com.example.PaITS.user.dto;


public class PublicUserResponse {

    private String username;
    private String email;
    private boolean isActive;

    public PublicUserResponse(String username, String email, boolean isActive) {
        this.username = username;
        this.email = email;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }
}