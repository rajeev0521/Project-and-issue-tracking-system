package com.example.PaITS.auth.service;

import com.example.PaITS.auth.dto.*;
import com.example.PaITS.auth.util.JwtUtil;
import com.example.PaITS.user.entity.User;
import com.example.PaITS.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account inactive");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        System.out.println("RAW PASSWORD: " + request.getPassword());
        System.out.println("DB HASH: " + user.getPasswordHash());
        System.out.println("MATCH: " + encoder.matches(request.getPassword(), user.getPasswordHash()));

        return new AuthResponse(token);
    }
}