package com.fuelpass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS configuration for the application
 */
@Configuration
public class CorsConfig {

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // Allow specific origins
//        configuration.setAllowedOriginPatterns(Arrays.asList(
//                "http://localhost:3000",
//                "http://localhost:3001",
//                "http://127.0.0.1:3000",
//                "http://127.0.0.1:3001"
//        ));
//
//        // Allow specific HTTP methods
//        configuration.setAllowedMethods(Arrays.asList(
//                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
//        ));
//
//        // Allow specific headers
//        configuration.setAllowedHeaders(Arrays.asList(
//                "Authorization",
//                "Content-Type",
//                "X-Requested-With",
//                "Accept",
//                "Origin",
//                "Access-Control-Request-Method",
//                "Access-Control-Request-Headers"
//        ));
//
//        // Allow credentials (cookies, authorization headers)
//        configuration.setAllowCredentials(true);
//
//        // Cache preflight response for 1 hour
//        configuration.setMaxAge(3600L);
//
//        // Apply to all endpoints
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
}