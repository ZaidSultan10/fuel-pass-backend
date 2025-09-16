package com.fuelpass.service;

import com.fuelpass.dto.request.LoginRequest;
import com.fuelpass.dto.response.AuthResponse;
import com.fuelpass.entity.User;
import com.fuelpass.repository.UserRepository;
import com.fuelpass.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticate user and return JWT tokens
     */
    public AuthResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        // Verify password
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid email or password");
//        }
        if(!Objects.equals(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Generate refresh token (you can implement this in JwtUtil)
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // Create and return AuthResponse
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(user.getId().toString())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    /**
     * Refresh JWT token using refresh token
     */
    public AuthResponse refreshToken(String refreshToken) {
        try {
            // Validate refresh token
            if (!jwtUtil.validateToken(refreshToken, null)) {
                throw new RuntimeException("Invalid refresh token");
            }

            // Get username from refresh token
            String email = jwtUtil.getUsernameFromToken(refreshToken);

            // Find user
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if user is still active
            if (!user.getIsActive()) {
                throw new RuntimeException("Account is deactivated");
            }

            // Generate new JWT token
            String newToken = jwtUtil.generateToken(user.getEmail());
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            // Create and return AuthResponse
            return AuthResponse.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .userId(user.getId().toString())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed: " + e.getMessage());
        }
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(String userId) {
        try {
            return userRepository.findById(java.util.UUID.fromString(userId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}