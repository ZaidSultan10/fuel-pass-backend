package com.fuelpass.config;

import com.fuelpass.entity.User;
import com.fuelpass.entity.UserRole;
import com.fuelpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Database initializer for creating default users and data
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultUsers();
    }

    /**
     * Initialize default users for testing
     */
    private void initializeDefaultUsers() {
        // Create default aircraft operator
        if (!userRepository.existsByEmail("operator@fuelpass.com")) {
            User aircraftOperator = new User();
            aircraftOperator.setEmail("operator@fuelpass.com");
            aircraftOperator.setPassword(passwordEncoder.encode("password123"));
            aircraftOperator.setRole(UserRole.AIRCRAFT_OPERATOR);
            aircraftOperator.setIsActive(true);
            userRepository.save(aircraftOperator);

            System.out.println("✓ Default Aircraft Operator created: operator@fuelpass.com / password123");
        }

        // Create default operations manager
        if (!userRepository.existsByEmail("manager@fuelpass.com")) {
            User operationsManager = new User();
            operationsManager.setEmail("manager@fuelpass.com");
            operationsManager.setPassword(passwordEncoder.encode("password123"));
            operationsManager.setRole(UserRole.OPERATIONS_MANAGER);
            operationsManager.setIsActive(true);
            userRepository.save(operationsManager);

            System.out.println("✓ Default Operations Manager created: manager@fuelpass.com / password123");
        }

        System.out.println("✓ Database initialization completed successfully!");
    }
}