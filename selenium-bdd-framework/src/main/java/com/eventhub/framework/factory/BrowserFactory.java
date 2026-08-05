package com.eventhub.framework.factory;

import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.constants.AppConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;

/**
 * Creates a fresh, configured WebDriver instance for a given browser name.
 * Uses WebDriverManager so no manual driver binaries need to be managed.
 */
public final class BrowserFactory {

    private static final Logger log = com.eventhub.framework.utilities.LogUtils.getLogger(BrowserFactory.class);

    private BrowserFactory() {
    }

    public static WebDriver createDriver(String browserName) {
        boolean headless = ConfigManager.getInstance().isHeadless();
        String normalized = browserName == null ? AppConstants.CHROME : browserName.trim().toLowerCase();
        log.info("Creating WebDriver instance for browser: {} (headless={})", normalized, headless);

        WebDriver driver;
        switch (normalized) {
            case AppConstants.FIREFOX -> {
                setupManagedDriverUnlessPreProvisioned("webdriver.gecko.driver", WebDriverManager.firefoxdriver());
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                driver = new FirefoxDriver(options);
            }
            case AppConstants.EDGE -> {
                setupManagedDriverUnlessPreProvisioned("webdriver.edge.driver", WebDriverManager.edgedriver());
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                driver = new EdgeDriver(options);
            }
            case AppConstants.CHROME -> {
                setupManagedDriverUnlessPreProvisioned("webdriver.chrome.driver", WebDriverManager.chromedriver());
                driver = new ChromeDriver(buildChromeOptions(headless));
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        return driver;
    }

    /**
     * WebDriverManager auto-downloads the matching driver binary from its
     * vendor CDN. On networks where that CDN is unreachable (e.g. a locked-down
     * CI runner), pre-provision the binary out of band and pass its path via
     * the standard -Dwebdriver.<browser>.driver system property - that skips
     * the network call entirely and uses the given binary as-is.
     */
    private static void setupManagedDriverUnlessPreProvisioned(String driverSystemProperty, WebDriverManager manager) {
        String preProvisionedPath = System.getProperty(driverSystemProperty);
        if (preProvisionedPath != null && !preProvisionedPath.isBlank()) {
            log.info("Using pre-provisioned driver binary from -D{}={}", driverSystemProperty, preProvisionedPath);
            return;
        }
        manager.setup();
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return options;
    }
}
