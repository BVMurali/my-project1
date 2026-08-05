package com.eventhub.framework.helpers;

import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.pages.LoginPage;
import com.eventhub.framework.utilities.LogUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

/**
 * Reusable authentication flow. Every scenario that needs an authenticated
 * session should call {@link #login(WebDriver)} (or {@link #loginAndVerify(WebDriver)}
 * if it also needs the post-login landing state confirmed) rather than
 * repeating the login steps inline.
 */
public class AuthenticationHelper {

    private static final Logger log = LogUtils.getLogger(AuthenticationHelper.class);

    private final WebDriver driver;

    public AuthenticationHelper(WebDriver driver) {
        this.driver = driver;
    }

    /** Logs in using the credentials configured for the active environment. */
    public LoginPage login() {
        ConfigManager config = ConfigManager.getInstance();
        return login(config.getTestUserEmail(), config.getTestUserPassword());
    }

    public LoginPage login(String email, String password) {
        log.info("Logging in as {}", email);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        return loginPage;
    }

    /**
     * Logs in and asserts the "Browse Events" link is visible, confirming a
     * successful login and that the app landed on the expected authenticated view.
     */
    public LoginPage loginAndVerify() {
        LoginPage loginPage = login();
        if (!loginPage.isBrowseEventsLinkVisible()) {
            throw new AssertionError("Login did not succeed: 'Browse Events' link is not visible");
        }
        log.info("Login verified - 'Browse Events' link is visible");
        return loginPage;
    }
}
