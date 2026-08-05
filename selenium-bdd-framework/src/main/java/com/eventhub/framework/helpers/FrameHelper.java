package com.eventhub.framework.helpers;

import com.eventhub.framework.utilities.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.time.Duration;

/** Centralizes iframe switching so page objects don't manage frame context directly. */
public class FrameHelper {

    private static final Logger log = LogUtils.getLogger(FrameHelper.class);

    private final WebDriver driver;

    public FrameHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void switchToFrame(By frameLocator) {
        WebElement frame = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(frameLocator));
        driver.switchTo().frame(frame);
        log.debug("Switched to frame located by {}", frameLocator);
    }

    public void switchToFrame(String nameOrId) {
        driver.switchTo().frame(nameOrId);
    }

    public void switchToFrame(int index) {
        driver.switchTo().frame(index);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }
}
