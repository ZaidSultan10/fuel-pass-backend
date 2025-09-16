package com.fuelpass.controller;

import com.fuelpass.dto.request.LoginRequest;
import com.fuelpass.dto.response.ApiResponse;
import com.fuelpass.dto.response.AuthResponse;
import com.fuelpass.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
                                                           HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request);

            // Set JWT token as HTTP-only cookie
            Cookie jwtCookie = new Cookie("jwt-token", authResponse.getToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Set to true in production with HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(jwtCookie);

            // Set refresh token as HTTP-only cookie
            Cookie refreshCookie = new Cookie("refresh-token", authResponse.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false); // Set to true in production with HTTPS
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        try {
            // Clear JWT token cookie
            Cookie jwtCookie = new Cookie("jwt-token", "");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0); // Expire immediately
            response.addCookie(jwtCookie);

            // Clear refresh token cookie
            Cookie refreshCookie = new Cookie("refresh-token", "");
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(0); // Expire immediately
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(ApiResponse.success("Logout successful", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Logout failed: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@CookieValue(value = "refresh-token", required = false) String refreshToken,
                                                                  HttpServletResponse response) {
        try {
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Refresh token not found"));
            }

            AuthResponse authResponse = authService.refreshToken(refreshToken);

            // Set new JWT token as HTTP-only cookie
            Cookie jwtCookie = new Cookie("jwt-token", authResponse.getToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token refresh failed: " + e.getMessage()));
        }
    }
}