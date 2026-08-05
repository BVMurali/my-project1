package com.eventhub.framework.helpers;

import com.eventhub.framework.utilities.JSUtils;
import com.eventhub.framework.utilities.LogUtils;
import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import java.util.List;

/**
 * Reusable common UI interactions shared by every Page Object. Wraps raw
 * Selenium calls with explicit waits and a JS-click fallback so tests stay
 * resilient to overlays/animations without ever resorting to Thread.sleep().
 */
public class UIActionsHelper {

    private static final Logger log = LogUtils.getLogger(UIActionsHelper.class);

    protected final WebDriver driver;

    public UIActionsHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void click(By locator) {
        WebElement element = WaitUtils.waitForClickable(driver, locator);
        try {
            element.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            log.warn("Native click failed for {} ({}), falling back to JS click", locator, e.getClass().getSimpleName());
            JSUtils.click(driver, WaitUtils.waitForVisible(driver, locator));
        }
    }

    public void click(WebElement element) {
        WaitUtils.waitForClickable(driver, element);
        try {
            element.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            log.warn("Native click failed on element ({}), falling back to JS click", e.getClass().getSimpleName());
            JSUtils.click(driver, element);
        }
    }

    public void type(By locator, String text) {
        WebElement element = WaitUtils.waitForVisible(driver, locator);
        element.clear();
        element.sendKeys(text);
    }

    public void type(WebElement element, String text) {
        WaitUtils.waitForVisible(driver, element, 10);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return WaitUtils.waitForVisible(driver, locator).getText();
    }

    public String getText(WebElement element) {
        return WaitUtils.waitForVisible(driver, element, 10).getText();
    }

    public boolean isVisible(By locator) {
        try {
            return WaitUtils.waitForVisible(driver, locator, 5).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVisible(By locator, int timeoutSeconds) {
        try {
            return WaitUtils.waitForVisible(driver, locator, timeoutSeconds).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits up to timeoutSeconds for the element to disappear. Returns false
     * (not true) on timeout - a TimeoutException here means the element was
     * still visible for the entire wait, so the "invisible" assertion failed.
     */
    public boolean isInvisible(By locator, int timeoutSeconds) {
        try {
            return WaitUtils.waitForInvisible(driver, locator, timeoutSeconds);
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public List<WebElement> findAll(By locator) {
        return WaitUtils.waitForAllVisible(driver, locator);
    }

    public WebElement find(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }

    /**
     * Resolves a form field (input/textarea/select) associated with a visible
     * &lt;label&gt; by its text, mirroring Playwright's getByLabel(). Tries the
     * label's "for" attribute first, then falls back to a nested/adjacent
     * field when no "for" attribute is present.
     */
    public WebElement findByLabel(String labelText) {
        By labelLocator = By.xpath(
                "//label[normalize-space(text())=\"" + labelText + "\"]");
        WebElement label = WaitUtils.waitForVisible(driver, labelLocator);
        String forAttribute = label.getDomAttribute("for");
        if (forAttribute != null && !forAttribute.isBlank()) {
            return WaitUtils.waitForVisible(driver, By.id(forAttribute));
        }
        By nestedOrFollowing = By.xpath(
                "//label[normalize-space(text())=\"" + labelText + "\"]"
                        + "//following::input[1] | //label[normalize-space(text())=\"" + labelText + "\"]"
                        + "//input | //label[normalize-space(text())=\"" + labelText + "\"]//textarea"
                        + " | //label[normalize-space(text())=\"" + labelText + "\"]//select");
        return WaitUtils.waitForVisible(driver, nestedOrFollowing);
    }

    public void typeByLabel(String labelText, String value) {
        type(findByLabel(labelText), value);
    }
}
