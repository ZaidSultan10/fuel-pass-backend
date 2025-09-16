package com.fuelpass.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 */
public class ValidationUtil {

    // Regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern ICAO_PATTERN = Pattern.compile("^[A-Z]{4}$");

    private static final Pattern TAIL_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9-]+$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate ICAO code format
     */
    public static boolean isValidIcaoCode(String icaoCode) {
        if (icaoCode == null || icaoCode.trim().isEmpty()) return false;
        return ICAO_PATTERN.matcher(icaoCode).matches();
    }

    /**
     * Validate tail number format
     */
    public static boolean isValidTailNumber(String tailNumber) {
        if (tailNumber == null || tailNumber.trim().isEmpty()) return false;
        return TAIL_NUMBER_PATTERN.matcher(tailNumber).matches() && tailNumber.length() <= 10;
    }

    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validate fuel volume
     */
    public static boolean isValidFuelVolume(Double volume) {
        return volume != null && volume > 0 && volume <= 100000;
    }

    /**
     * Validate delivery time window
     */
    public static boolean isValidDeliveryWindow(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        if (start == null || end == null) return false;
        if (start.isAfter(end)) return false;

        long hoursBetween = java.time.Duration.between(start, end).toHours();
        return hoursBetween <= 24;
    }

    /**
     * Validate string length
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) return minLength == 0;
        return value.length() >= minLength && value.length() <= maxLength;
    }

    /**
     * Validate positive number
     */
    public static boolean isPositive(Number value) {
        return value != null && value.doubleValue() > 0;
    }

    /**
     * Validate non-negative number
     */
    public static boolean isNonNegative(Number value) {
        return value != null && value.doubleValue() >= 0;
    }

    /**
     * Validate UUID format
     */
    public static boolean isValidUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) return false;
        try {
            java.util.UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Sanitize string input
     */
    public static String sanitizeString(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"'&]", "");
    }

    /**
     * Validate pagination parameters
     */
    public static boolean isValidPagination(int page, int size) {
        return page >= 0 && size > 0 && size <= 100;
    }

    /**
     * Validate sort parameters
     */
    public static boolean isValidSort(String sortBy, String sortOrder) {
        if (sortBy == null || sortBy.trim().isEmpty()) return false;
        if (sortOrder == null || sortOrder.trim().isEmpty()) return false;

        return sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc");
    }
}