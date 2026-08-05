package com.eventhub.framework.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date/time helpers used across the framework: unique-name timestamps,
 * report timestamps, and generation of future event dates for the
 * "Event Date & Time" (HTML5 datetime-local) field on the admin form.
 */
public final class DateTimeUtils {

    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter DATETIME_LOCAL = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    private DateTimeUtils() {
    }

    public static String currentTimestampForFileName() {
        return LocalDateTime.now().format(FILE_TIMESTAMP);
    }

    /** Returns a datetime-local (yyyy-MM-ddTHH:mm) value 7 days in the future. */
    public static String futureDateValue() {
        return futureDateValue(7);
    }

    /** Returns a datetime-local (yyyy-MM-ddTHH:mm) value {@code daysAhead} days in the future. */
    public static String futureDateValue(int daysAhead) {
        LocalDateTime future = LocalDateTime.now().plusDays(daysAhead).withMinute(0).withSecond(0).withNano(0);
        return future.format(DATETIME_LOCAL);
    }

    public static String nowForDisplay() {
        return LocalDateTime.now().format(DISPLAY);
    }

    public static long currentEpochMillis() {
        return System.currentTimeMillis();
    }
}
