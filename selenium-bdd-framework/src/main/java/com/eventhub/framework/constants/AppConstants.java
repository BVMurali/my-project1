package com.eventhub.framework.constants;

/**
 * Framework-wide constant values. Keep environment/URL specific values in
 * config.properties / environments/*.properties instead of hardcoding here.
 */
public final class AppConstants {

    private AppConstants() {
    }

    public static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    public static final String ENVIRONMENTS_DIR = "src/main/resources/environments/";
    public static final String TEST_DATA_DIR = "src/main/resources/testdata/";

    public static final String SCREENSHOT_DIR = "screenshots/";
    public static final String REPORT_DIR = "reports/";
    public static final String LOG_DIR = "logs/";

    public static final int DEFAULT_IMPLICIT_WAIT_SECONDS = 5;
    public static final int DEFAULT_EXPLICIT_WAIT_SECONDS = 15;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS = 30;

    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE = "edge";

    // Application routes (relative to base.url)
    public static final String LOGIN_PATH = "/login";
    public static final String ADMIN_EVENTS_PATH = "/admin/events";
    public static final String EVENTS_PATH = "/events";
    public static final String BOOKINGS_PATH = "/bookings";
}
