package com.eventhub.framework.utilities;

import com.eventhub.framework.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Centralized explicit-wait utility. Every page object and helper should
 * route waits through here instead of using Thread.sleep().
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait wait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    private static WebDriverWait defaultWait(WebDriver driver) {
        return wait(driver, ConfigManager.getInstance().getExplicitWait());
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return defaultWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator, int timeoutSeconds) {
        return wait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebDriver driver, WebElement element, int timeoutSeconds) {
        return wait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    public static boolean waitForInvisible(WebDriver driver, By locator, int timeoutSeconds) {
        return wait(driver, timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return defaultWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, WebElement element) {
        return defaultWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static List<WebElement> waitForAllVisible(WebDriver driver, By locator) {
        return defaultWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForTextToBe(WebDriver driver, By locator, String text) {
        return defaultWait(driver).until(ExpectedConditions.textToBe(locator, text));
    }

    public static boolean waitForUrlToBe(WebDriver driver, String url) {
        return defaultWait(driver).until(ExpectedConditions.urlToBe(url));
    }

    public static <T> T waitFor(WebDriver driver, int timeoutSeconds, Function<WebDriver, T> condition) {
        return wait(driver, timeoutSeconds).until(condition);
    }
}
