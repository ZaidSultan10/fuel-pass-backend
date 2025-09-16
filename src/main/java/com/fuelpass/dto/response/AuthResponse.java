package com.fuelpass.dto.response;

import com.fuelpass.entity.UserRole;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String userId;
    private String email;
    private Boolean isActive;
    private UserRole role;

    // Default constructor
    public AuthResponse() {}

    // Constructor with all fields
    public AuthResponse(String token, String refreshToken, String userId, String email, Boolean isActive, UserRole role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.isActive = isActive;
        this.role = role;
    }

    // Builder class
    public static class Builder {
        private String token;
        private String refreshToken;
        private String userId;
        private String email;
        private Boolean isActive;
        private UserRole role;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, refreshToken, userId, email, isActive, role);
        }
    }

    // Static method to create builder
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", isActive='" + isActive + '\'' +
                ", role=" + role +
                '}';
    }
}