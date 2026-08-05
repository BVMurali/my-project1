package com.eventhub.framework.helpers;

import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/** Wraps Selenium's {@link Select} for native &lt;select&gt; dropdowns. */
public class DropdownHelper {

    private final WebDriver driver;

    public DropdownHelper(WebDriver driver) {
        this.driver = driver;
    }

    private Select select(By locator) {
        return new Select(WaitUtils.waitForVisible(driver, locator));
    }

    public void selectByVisibleText(By locator, String visibleText) {
        select(locator).selectByVisibleText(visibleText);
    }

    public void selectByValue(By locator, String value) {
        select(locator).selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        select(locator).selectByIndex(index);
    }

    public String getSelectedOption(By locator) {
        return select(locator).getFirstSelectedOption().getText();
    }

    public List<String> getAllOptions(By locator) {
        return select(locator).getOptions().stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean isMultiple(By locator) {
        return select(locator).isMultiple();
    }
}
