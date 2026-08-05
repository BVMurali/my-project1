package com.eventhub.framework.helpers;

import com.eventhub.framework.utilities.LogUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/** Handles multi-window / multi-tab scenarios. */
public class WindowHelper {

    private static final Logger log = LogUtils.getLogger(WindowHelper.class);

    private final WebDriver driver;
    private String parentWindowHandle;

    public WindowHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void rememberCurrentWindow() {
        parentWindowHandle = driver.getWindowHandle();
    }

    public void switchToNewWindow() {
        String original = parentWindowHandle != null ? parentWindowHandle : driver.getWindowHandle();
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        handles.remove(original);
        if (handles.isEmpty()) {
            throw new IllegalStateException("No new window/tab was opened");
        }
        driver.switchTo().window(handles.get(handles.size() - 1));
        log.debug("Switched to new window: {}", driver.getCurrentUrl());
    }

    public void switchToWindowByTitle(String title) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(title)) {
                return;
            }
        }
        throw new IllegalStateException("No window found with title: " + title);
    }

    public void closeCurrentAndSwitchToParent() {
        driver.close();
        if (parentWindowHandle != null) {
            driver.switchTo().window(parentWindowHandle);
        }
    }

    public int getOpenWindowCount() {
        return driver.getWindowHandles().size();
    }
}
