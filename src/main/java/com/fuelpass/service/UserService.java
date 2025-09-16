package com.fuelpass.service;

import com.fuelpass.entity.User;
import com.fuelpass.entity.UserRole;
import com.fuelpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for user operations
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID
     */
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndIsActiveTrue(email);
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRoleAndIsActiveTrue(role);
    }

    /**
     * Create a new user
     */
    public User createUser(String email, String password, UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setIsActive(true);

        return userRepository.save(user);
    }

    /**
     * Update user
     */
    public User updateUser(UUID userId, UpdateUserRequest request) {
        User user = getUserById(userId);

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("User with email " + request.getEmail() + " already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        return userRepository.save(user);
    }

    /**
     * Deactivate user
     */
    public void deactivateUser(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Activate user
     */
    public void activateUser(UUID userId) {
        User user = getUserById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }

    /**
     * Change user password
     */
    public void changePassword(UUID userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Get user statistics
     */
    public UserStatistics getUserStatistics() {
        UserStatistics statistics = new UserStatistics();

        statistics.setTotalUsers(userRepository.count());
        statistics.setAircraftOperators(userRepository.countByRoleAndIsActiveTrue(UserRole.AIRCRAFT_OPERATOR));
        statistics.setOperationsManagers(userRepository.countByRoleAndIsActiveTrue(UserRole.OPERATIONS_MANAGER));
        statistics.setActiveUsers(userRepository.countByIsActiveTrue());

        return statistics;
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // DTOs
    public static class UpdateUserRequest {
        private String name;
        private String email;
        private UserRole role;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }

    public static class UserStatistics {
        private long totalUsers;
        private long aircraftOperators;
        private long operationsManagers;
        private long activeUsers;

        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getAircraftOperators() { return aircraftOperators; }
        public void setAircraftOperators(long aircraftOperators) { this.aircraftOperators = aircraftOperators; }
        public long getOperationsManagers() { return operationsManagers; }
        public void setOperationsManagers(long operationsManagers) { this.operationsManagers = operationsManagers; }
        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
    }
}