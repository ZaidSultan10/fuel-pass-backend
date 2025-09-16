package com.fuelpass.entity;

/**
 * Enum representing user roles in the system
 */
public enum UserRole {
    AIRCRAFT_OPERATOR("Aircraft Operator"),
    OPERATIONS_MANAGER("Operations Manager");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}