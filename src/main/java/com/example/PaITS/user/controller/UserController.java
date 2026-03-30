package com.example.PaITS.user.controller;


import com.example.PaITS.user.dto.CreateUserRequest;

import com.example.PaITS.user.dto.PublicUserResponse;
import com.example.PaITS.user.dto.UpdateUserRequest;
import com.example.PaITS.user.dto.UserResponse;
import com.example.PaITS.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // ✅ CREATE USER (REGISTER)
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.ok(userService.createUser(request));
    }

    // ✅ GET USER BY ID
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponse> getUserById(
//            @PathVariable UUID id) {
//
//        return ResponseEntity.ok(userService.getUserById(id));
//    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(userService.getByEmail(email));
    }


    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deactivateMe() {

        userService.deactivateCurrentUser();
        return ResponseEntity.ok("Account deactivated");
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PublicUserResponse> getPublicUser(
            @PathVariable UUID id) {

        return ResponseEntity.ok(userService.getPublicUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted");
    }



}

