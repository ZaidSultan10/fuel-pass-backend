package com.fuelpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for FuelPass - Fuel Order Management System
 *
 * This application provides a comprehensive fuel order management system
 * for aircraft operators and operations managers with the following features:
 * - JWT-based authentication with role-based access control
 * - Fuel order creation, management, and status tracking
 * - Real-time order statistics and dashboard
 * - Secure API endpoints with proper validation
 *
 * @author FuelPass Team
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties
public class FuelPassApplication {

    /**
     * Main method to start the Spring Boot application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FuelPassApplication.class, args);
    }
}