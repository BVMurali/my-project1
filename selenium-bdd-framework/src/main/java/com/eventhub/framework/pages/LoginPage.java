package com.eventhub.framework.pages;

import com.eventhub.framework.base.BasePage;
import com.eventhub.framework.constants.AppConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for /login. Encapsulates locators/actions only - assertions
 * live in the step definitions.
 */
public class LoginPage extends BasePage {

    private static final By EMAIL_INPUT = By.cssSelector("input[placeholder='you@email.com']");
    private static final By LOGIN_BUTTON = By.id("login-btn");
    private static final By BROWSE_EVENTS_LINK = By.partialLinkText("Browse Events");
    private static final String PASSWORD_LABEL = "Password";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        navigateToPath(AppConstants.LOGIN_PATH);
    }

    public void enterEmail(String email) {
        type(EMAIL_INPUT, email);
    }

    public void enterPassword(String password) {
        typeByLabel(PASSWORD_LABEL, password);
    }

    public void clickLogin() {
        click(LOGIN_BUTTON);
    }

    public boolean isBrowseEventsLinkVisible() {
        return isVisible(BROWSE_EVENTS_LINK, 10);
    }
}
