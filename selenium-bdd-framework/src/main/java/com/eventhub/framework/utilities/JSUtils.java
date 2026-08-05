package com.eventhub.framework.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * JavaScript executor helpers for scenarios where native Selenium
 * interactions are unreliable (overlays, lazy-loaded elements, sticky
 * headers, etc.).
 */
public final class JSUtils {

    private JSUtils() {
    }

    private static JavascriptExecutor js(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    public static void click(WebDriver driver, WebElement element) {
        js(driver).executeScript("arguments[0].click();", element);
    }

    public static void setValue(WebDriver driver, WebElement element, String value) {
        js(driver).executeScript("arguments[0].value = arguments[1];", element, value);
    }

    /**
     * Sets an input's value bypassing native key-by-key typing, then fires
     * input/change events so framework-controlled components (React, etc.)
     * pick up the change. Necessary for widgets where sendKeys() is unreliable -
     * e.g. segmented native &lt;input type="datetime-local"&gt; controls, where
     * typing a literal ISO string character-by-character gets misinterpreted
     * by the browser's segment auto-advance behavior.
     */
    public static void setControlledInputValue(WebDriver driver, WebElement element, String value) {
        String script =
                "const element = arguments[0];" +
                "const value = arguments[1];" +
                "const prototype = Object.getPrototypeOf(element);" +
                "const nativeSetter = Object.getOwnPropertyDescriptor(prototype, 'value')?.set;" +
                "if (nativeSetter) { nativeSetter.call(element, value); } else { element.value = value; }" +
                "element.dispatchEvent(new Event('input', { bubbles: true }));" +
                "element.dispatchEvent(new Event('change', { bubbles: true }));";
        js(driver).executeScript(script, element, value);
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        js(driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }

    public static void scrollToTop(WebDriver driver) {
        js(driver).executeScript("window.scrollTo(0, 0);");
    }

    public static void highlight(WebDriver driver, WebElement element) {
        js(driver).executeScript("arguments[0].style.border='3px solid red';", element);
    }

    public static String getPageTitle(WebDriver driver) {
        return (String) js(driver).executeScript("return document.title;");
    }

    public static Object executeScript(WebDriver driver, String script, Object... args) {
        return js(driver).executeScript(script, args);
    }

    public static boolean isPageLoadComplete(WebDriver driver) {
        return "complete".equals(js(driver).executeScript("return document.readyState"));
    }
}
