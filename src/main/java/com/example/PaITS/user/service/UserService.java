package com.example.PaITS.user.service;

import com.example.PaITS.user.dto.*;
import com.example.PaITS.user.entity.Role;
import com.example.PaITS.user.entity.User;
import com.example.PaITS.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;



@Service

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.MEMBER);
        user.setActive(true);


        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }


    public UserResponse updateCurrentUser( UpdateUserRequest request) {



        String currentEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 SECURITY CHECK
        if (!user.getEmail().equals(currentEmail)) {
            throw new RuntimeException("You can only update your own account");
        }
        if (request.getFullName() != null)
            user.setFullName(request.getFullName());

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());

        if (request.getPassword() != null)
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        if (request.getAvatarUrl() != null)
            user.setAvatarUrl(request.getAvatarUrl());

        User updated = userRepository.save(user);
        return mapToResponse(updated);
    }

    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }



    public void deactivateCurrentUser() {

        String currentEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // 3. Deactivate account
        user.setActive(false);

        // 4. Save changes
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.isActive(),
                user.getAvatarUrl()
        );
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // we stored email in JWT
    }
    public UserResponse getByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }


    public UserResponse getCurrentUser() {

        String currentEmail = getCurrentUserEmail();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    public PublicUserResponse getPublicUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new PublicUserResponse(
                user.getUsername(),
                user.getEmail(),
                user.isActive()
        );
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteUserById(UUID id) {

        String currentEmail = getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Admin cannot delete himself");
        }

        targetUser.setActive(false);
        userRepository.save(targetUser);
    }
}
