package com.eventhub.framework.config;

import com.eventhub.framework.constants.AppConstants;
import com.eventhub.framework.utilities.EnvironmentUtils;
import com.eventhub.framework.utilities.PropertyReaderUtils;

import java.util.Properties;

/**
 * Central, singleton access point for all framework configuration.
 * Merges src/main/resources/config.properties (defaults) with the
 * active environment's property file, and finally with any -D system
 * property overrides supplied on the Maven / JVM command line.
 */
public final class ConfigManager {

    private static volatile ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        properties.putAll(PropertyReaderUtils.loadFromFile(AppConstants.CONFIG_FILE_PATH));
        properties.putAll(PropertyReaderUtils.loadFromFile(EnvironmentUtils.getEnvironmentFilePath()));
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /** System property (-Dkey=value) takes precedence over file-based config. */
    public String get(String key) {
        String override = System.getProperty(key);
        return (override != null && !override.isBlank()) ? override : properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public String getBrowser() {
        return get("browser", AppConstants.CHROME);
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(get("implicit.wait", String.valueOf(AppConstants.DEFAULT_IMPLICIT_WAIT_SECONDS)));
    }

    public int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait", String.valueOf(AppConstants.DEFAULT_EXPLICIT_WAIT_SECONDS)));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout", String.valueOf(AppConstants.DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS)));
    }

    public boolean isParallelExecutionEnabled() {
        return Boolean.parseBoolean(get("parallel.execution", "false"));
    }

    public int getThreadCount() {
        return Integer.parseInt(get("thread.count", "1"));
    }

    public String getTestUserEmail() {
        return get("test.user.email");
    }

    public String getTestUserPassword() {
        return get("test.user.password");
    }
}
