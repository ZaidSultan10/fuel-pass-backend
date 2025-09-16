package com.fuelpass.exception;

import java.util.List;

/**
 * Exception thrown when validation fails
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<String> validationErrors;

    /**
     * Constructs a new ValidationException with the specified detail message
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and validation errors
     *
     * @param message the detail message
     * @param validationErrors the list of validation errors
     */
    public ValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    /**
     * Constructs a new ValidationException with the specified detail message, cause, and validation errors
     *
     * @param message the detail message
     * @param cause the cause
     * @param validationErrors the list of validation errors
     */
    public ValidationException(String message, Throwable cause, List<String> validationErrors) {
        super(message, cause);
        this.validationErrors = validationErrors;
    }

    /**
     * Get the validation errors
     *
     * @return the list of validation errors
     */
    public List<String> getValidationErrors() {
        return validationErrors;
    }

    /**
     * Set the validation errors
     *
     * @param validationErrors the list of validation errors
     */
    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    /**
     * Constructs a new ValidationException for a specific field
     *
     * @param fieldName the name of the field
     * @param fieldValue the value of the field
     * @param reason the reason for validation failure
     */
    public ValidationException(String fieldName, String fieldValue, String reason) {
        super(String.format("Validation failed for field '%s' with value '%s': %s", fieldName, fieldValue, reason));
    }

    /**
     * Constructs a new ValidationException for a specific field with a custom message
     *
     * @param fieldName the name of the field
     * @param message the validation message
     */
    public ValidationException(String fieldName, String message) {
        super(String.format("Validation failed for field '%s': %s", fieldName, message));
    }
}