package com.fuelpass.controller;

import com.fuelpass.dto.response.ApiResponse;
import com.fuelpass.entity.User;
import com.fuelpass.entity.UserRole;
import com.fuelpass.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for user management endpoints
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users (Operations Manager only)
     */
    @GetMapping
    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage()));
        }
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable UUID userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found: " + e.getMessage()));
        }
    }

    /**
     * Get users by role (Operations Manager only)
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable UserRole role) {
        try {
            List<User> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve users: " + e.getMessage()));
        }
    }

    /**
     * Create a new user (Operations Manager only)
     */
    @PostMapping
    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            User newUser = userService.createUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getRole()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", newUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create user: " + e.getMessage()));
        }
    }

    /**
     * Update user (Operations Manager only)
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID userId,
                                                        @Valid @RequestBody UpdateUserRequest request) {
        try {
            // Convert controller DTO to service DTO
            UserService.UpdateUserRequest serviceRequest = new UserService.UpdateUserRequest();
            serviceRequest.setEmail(request.getEmail());
            serviceRequest.setRole(request.getRole());

            User updatedUser = userService.updateUser(userId, serviceRequest);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update user: " + e.getMessage()));
        }
    }

    /**
     * Deactivate user (Operations Manager only)
     */
    @PatchMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID userId) {
        try {
            userService.deactivateUser(userId);
            return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to deactivate user: " + e.getMessage()));
        }
    }

    /**
     * Get user statistics (Operations Manager only)
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    public ResponseEntity<ApiResponse<UserService.UserStatistics>> getUserStatistics() {
        try {
            System.out.println("USercontroller.getOrderStatistics" + userService.getUserStatistics() + " --- "+ userService);

            UserService.UserStatistics statistics = userService.getUserStatistics();
            return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve statistics: " + e.getMessage()));
        }
    }

    // DTOs for request/response
    public static class CreateUserRequest {
        private String email;
        private String password;
        private UserRole role;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }

    public static class UpdateUserRequest {
        private String email;
        private UserRole role;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }
}