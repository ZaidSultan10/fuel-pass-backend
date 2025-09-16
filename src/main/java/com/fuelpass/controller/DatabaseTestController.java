package com.fuelpass.controller;

import com.fuelpass.dto.response.ApiResponse;
import com.fuelpass.entity.User;
import com.fuelpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for testing database connectivity
 */
@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
public class DatabaseTestController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Test database connectivity
     */
    @GetMapping("/database")
    public ResponseEntity<ApiResponse<String>> testDatabase() {
        try {
            long userCount = userRepository.count();
            return ResponseEntity.ok(ApiResponse.success("Database connection successful. User count: " + userCount));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Database connection failed: " + e.getMessage()));
        }
    }

    /**
     * Get all users (for testing)
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage()));
        }
    }
}