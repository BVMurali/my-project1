package com.eventhub.framework.base;

import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.helpers.UIActionsHelper;
import com.eventhub.framework.utilities.LogUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

/**
 * Superclass for every Page Object. Provides the WebDriver instance, common
 * UI actions (via {@link UIActionsHelper}), and page-level navigation.
 * Page Objects should hold ONLY locators and page actions - no assertions
 * or business/test logic, which belong in Step Definitions.
 */
public abstract class BasePage extends UIActionsHelper {

    protected final Logger log = LogUtils.getLogger(getClass());
    protected final String baseUrl;

    protected BasePage(WebDriver driver) {
        super(driver);
        this.baseUrl = ConfigManager.getInstance().getBaseUrl();
        PageFactory.initElements(driver, this);
    }

    protected void navigateToPath(String path) {
        String url = baseUrl + path;
        log.info("Navigating to {}", url);
        driver.navigate().to(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
