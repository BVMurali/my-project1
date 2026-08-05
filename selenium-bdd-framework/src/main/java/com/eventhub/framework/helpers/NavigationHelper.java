package com.eventhub.framework.helpers;

import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.utilities.LogUtils;
import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

/**
 * Centralizes URL construction and browser navigation (back/forward/refresh)
 * so page objects and step definitions never hardcode base.url.
 */
public class NavigationHelper {

    private static final Logger log = LogUtils.getLogger(NavigationHelper.class);

    private final WebDriver driver;
    private final String baseUrl;

    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
        this.baseUrl = ConfigManager.getInstance().getBaseUrl();
    }

    public void goToPath(String path) {
        String url = baseUrl + path;
        log.info("Navigating to {}", url);
        driver.get(url);
    }

    public void goToUrl(String url) {
        log.info("Navigating to absolute URL {}", url);
        driver.get(url);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void back() {
        driver.navigate().back();
    }

    public void forward() {
        driver.navigate().forward();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isOnPath(String path, int timeoutSeconds) {
        return WaitUtils.waitForUrlToBe(driver, baseUrl + path);
    }
}
