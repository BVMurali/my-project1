package com.eventhub.framework.hooks;

import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.context.ScenarioContext;
import com.eventhub.framework.factory.DriverManager;
import com.eventhub.framework.utilities.LogUtils;
import com.eventhub.framework.utilities.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;

/**
 * Cucumber lifecycle hooks: browser bring-up/teardown, failure screenshots
 * (attached to Allure/Extent/Cucumber reports via Scenario.attach), and
 * scenario-level logging. Runs once per scenario so parallel execution gets
 * an isolated WebDriver per thread (see {@link DriverManager}).
 */
public class Hooks {

    private static final Logger log = LogUtils.getLogger(Hooks.class);

    private final ScenarioContext scenarioContext;

    public Hooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void setUp(Scenario scenario) {
        log.info("========== STARTING SCENARIO: {} [{}] ==========", scenario.getName(), scenario.getSourceTagNames());
        scenarioContext.clear();
        DriverManager.initDriver(ConfigManager.getInstance().getBrowser());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (scenario.isFailed()) {
                attachScreenshot(driver, scenario);
                attachBrowserConsoleLogs(driver, scenario);
            }
        } catch (Exception e) {
            log.warn("Failed while collecting failure diagnostics for scenario '{}'", scenario.getName(), e);
        } finally {
            log.info("========== FINISHED SCENARIO: {} - STATUS: {} ==========", scenario.getName(), scenario.getStatus());
            DriverManager.quitDriver();
        }
    }

    private void attachScreenshot(WebDriver driver, Scenario scenario) {
        byte[] screenshot = ScreenshotUtils.captureAsBytes(driver);
        scenario.attach(screenshot, "image/png", scenario.getName());
        ScreenshotUtils.captureAndSave(driver, scenario.getName());
    }

    private void attachBrowserConsoleLogs(WebDriver driver, Scenario scenario) {
        try {
            LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
            StringBuilder builder = new StringBuilder();
            for (LogEntry entry : entries) {
                builder.append(entry.toString()).append(System.lineSeparator());
            }
            if (builder.length() > 0) {
                scenario.attach(builder.toString(), "text/plain", "browser-console-log");
            }
        } catch (Exception e) {
            log.debug("Browser console logs unavailable for this driver/browser combination", e);
        }
    }
}
