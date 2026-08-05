package com.eventhub.framework.factory;

import com.eventhub.framework.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import java.time.Duration;

/**
 * Owns the WebDriver lifecycle per thread. Backed by ThreadLocal so that
 * parallel scenario execution (Cucumber's dynamic parallel strategy) never
 * shares a browser session across threads.
 */
public final class DriverManager {

    private static final Logger log = com.eventhub.framework.utilities.LogUtils.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver(String browserName) {
        if (DRIVER_THREAD_LOCAL.get() != null) {
            log.warn("Driver already initialized on thread {}, quitting stale instance first", Thread.currentThread().getId());
            quitDriver();
        }
        WebDriver driver = BrowserFactory.createDriver(browserName);
        ConfigManager config = ConfigManager.getInstance();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();
        DRIVER_THREAD_LOCAL.set(driver);
        log.info("WebDriver initialized on thread {}", Thread.currentThread().getId());
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized on thread " + Thread.currentThread().getId()
                    + ". Call DriverManager.initDriver() in a @Before hook first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER_THREAD_LOCAL.remove();
                log.info("WebDriver quit and removed from thread {}", Thread.currentThread().getId());
            }
        }
    }
}
