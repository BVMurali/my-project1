package com.eventhub.framework.pages;

import com.eventhub.framework.base.BasePage;
import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.constants.AppConstants;
import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for /bookings - the authenticated user's booking history.
 */
public class MyBookingsPage extends BasePage {

    private static final By BOOKING_CARD = By.cssSelector("#booking-card");
    private static final By BOOKING_REF_IN_CARD = By.cssSelector(".booking-ref");
    private static final By VIEW_DETAILS_LINK = By.partialLinkText("View Details");

    public MyBookingsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnBookingsPage() {
        String expectedUrl = ConfigManager.getInstance().getBaseUrl() + AppConstants.BOOKINGS_PATH;
        return WaitUtils.waitForUrlToBe(driver, expectedUrl);
    }

    public List<WebElement> getAllBookingCards() {
        return findAll(BOOKING_CARD);
    }

    public boolean isFirstBookingCardVisible() {
        return isVisible(BOOKING_CARD, 10);
    }

    public WebElement findCardByBookingRef(String bookingRef) {
        return getAllBookingCards().stream()
                .filter(card -> {
                    try {
                        return card.findElement(BOOKING_REF_IN_CARD).getText().trim().equals(bookingRef);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No booking card found with booking ref: " + bookingRef));
    }

    public boolean cardContainsText(WebElement card, String text) {
        return card.getText().contains(text);
    }

    public void clickFirstViewDetails() {
        click(VIEW_DETAILS_LINK);
    }
}
