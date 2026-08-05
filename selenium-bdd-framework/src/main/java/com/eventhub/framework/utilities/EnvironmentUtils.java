package com.eventhub.framework.utilities;

import com.eventhub.framework.constants.AppConstants;

/**
 * Resolves the active test environment (qa | dev | staging) and exposes the
 * path to its property file. Resolution order: -Denv system property,
 * ENV shell variable, then "qa" as the default.
 */
public final class EnvironmentUtils {

    private static final String DEFAULT_ENV = "qa";

    private EnvironmentUtils() {
    }

    public static String getActiveEnvironment() {
        String env = System.getProperty("env");
        if (env == null || env.isBlank()) {
            env = System.getenv("ENV");
        }
        if (env == null || env.isBlank()) {
            env = DEFAULT_ENV;
        }
        return env.toLowerCase();
    }

    public static String getEnvironmentFilePath() {
        return AppConstants.ENVIRONMENTS_DIR + getActiveEnvironment() + ".properties";
    }
}
