package com.eventhub.framework.helpers;

import com.eventhub.framework.utilities.LogUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.time.Duration;

/** Handles native JavaScript alerts/confirms/prompts. */
public class AlertHelper {

    private static final Logger log = LogUtils.getLogger(AlertHelper.class);

    private final WebDriver driver;

    public AlertHelper(WebDriver driver) {
        this.driver = driver;
    }

    private Alert waitForAlert(int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(ExpectedConditions.alertIsPresent());
    }

    public void accept() {
        waitForAlert(10).accept();
        log.info("Alert accepted");
    }

    public void dismiss() {
        waitForAlert(10).dismiss();
        log.info("Alert dismissed");
    }

    public String getText() {
        return waitForAlert(10).getText();
    }

    public void typeAndAccept(String text) {
        Alert alert = waitForAlert(10);
        alert.sendKeys(text);
        alert.accept();
    }

    public boolean isAlertPresent() {
        try {
            waitForAlert(3);
            return true;
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
