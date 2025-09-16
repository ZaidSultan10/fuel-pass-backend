package com.fuelpass.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date operations
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Format LocalDateTime to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Format LocalDateTime to date string
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_FORMATTER);
    }

    /**
     * Format LocalDateTime to time string
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(TIME_FORMATTER);
    }

    /**
     * Parse string to LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) return null;
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    /**
     * Get current timestamp
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Add hours to date
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) return null;
        return dateTime.plusHours(hours);
    }

    /**
     * Add days to date
     */
    public static LocalDateTime addDays(LocalDateTime dateTime, long days) {
        if (dateTime == null) return null;
        return dateTime.plusDays(days);
    }

    /**
     * Calculate difference in hours between two dates
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.HOURS.between(start, end);
    }

    /**
     * Calculate difference in days between two dates
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Check if date is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isAfter(now());
    }

    /**
     * Check if date is in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isBefore(now());
    }

    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        LocalDateTime today = now();
        return dateTime.toLocalDate().equals(today.toLocalDate());
    }

    /**
     * Get start of day
     */
    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * Get end of day
     */
    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.toLocalDate().atTime(23, 59, 59);
    }
}