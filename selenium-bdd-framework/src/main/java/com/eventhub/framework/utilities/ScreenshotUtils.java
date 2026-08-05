package com.eventhub.framework.utilities;

import com.eventhub.framework.constants.AppConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Captures screenshots to disk (used by Hooks on scenario failure and
 * attached to Allure/Extent/Cucumber reports).
 */
public final class ScreenshotUtils {

    private static final Logger log = LogUtils.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    public static byte[] captureAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static String captureAndSave(WebDriver driver, String scenarioName) {
        try {
            Files.createDirectories(Path.of(AppConstants.SCREENSHOT_DIR));
            String sanitizedName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
            String fileName = sanitizedName + "_" + DateTimeUtils.currentTimestampForFileName() + ".png";
            Path destination = Path.of(AppConstants.SCREENSHOT_DIR, fileName);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), destination);
            log.info("Screenshot saved: {}", destination);
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("Failed to capture/save screenshot", e);
            return null;
        }
    }
}
