package com.fuelpass.exception;

/**
 * Exception thrown when a user is not authorized to perform an action
 */
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UnauthorizedException with the specified detail message
     *
     * @param message the detail message
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthorizedException with the specified detail message and cause
     *
     * @param message the detail message
     * @param cause the cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new UnauthorizedException for a specific action and resource
     *
     * @param action the action that was attempted
     * @param resource the resource that was accessed
     */
    public static UnauthorizedException forAction(String action, String resource) {
        return new UnauthorizedException(String.format("Unauthorized to %s %s", action, resource));
    }

    /**
     * Constructs a new UnauthorizedException for a specific user and action
     *
     * @param userId the ID of the user
     * @param action the action that was attempted
     */
    public static UnauthorizedException forUser(String userId, String action) {
        return new UnauthorizedException(String.format("User %s is not authorized to %s", userId, action));
    }

    /**
     * Constructs a new UnauthorizedException for a specific role requirement
     *
     * @param requiredRole the role that is required
     * @param action the action that was attempted
     */
    public static UnauthorizedException forRole(String requiredRole, String action) {
        return new UnauthorizedException(String.format("Role %s is required to %s", requiredRole, action));
    }

    /**
     * Constructs a new UnauthorizedException for insufficient permissions
     *
     * @param currentRole the current user's role
     * @param requiredRole the required role
     */
    public static UnauthorizedException insufficientPermissions(String currentRole, String requiredRole) {
        return new UnauthorizedException(String.format("Insufficient permissions. Current role: %s, Required role: %s", currentRole, requiredRole));
    }

    /**
     * Constructs a new UnauthorizedException for access to specific resource
     *
     * @param resourceType the type of resource
     * @param resourceId the ID of the resource
     * @param action the action attempted
     */
    public static UnauthorizedException forResource(String resourceType, String resourceId, String action) {
        return new UnauthorizedException(String.format("Unauthorized to %s %s with ID: %s", action, resourceType, resourceId));
    }
}