package com.fuelpass.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and ID
     *
     * @param resourceType the type of resource (e.g., "User", "FuelOrder")
     * @param resourceId the ID of the resource
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId));
    }

    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and field
     *
     * @param resourceType the type of resource (e.g., "User", "FuelOrder")
     * @param fieldName the field name (e.g., "email", "tailNumber")
     * @param fieldValue the field value
     */
    public ResourceNotFoundException(String resourceType, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", resourceType, fieldName, fieldValue));
    }
}