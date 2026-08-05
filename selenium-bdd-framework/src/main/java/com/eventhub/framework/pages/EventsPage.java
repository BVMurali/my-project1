package com.eventhub.framework.pages;

import com.eventhub.framework.base.BasePage;
import com.eventhub.framework.constants.AppConstants;
import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Page Object for the public /events listing page. Provides card-level
 * lookups (by title) and per-card actions (Book Now, seat count parsing).
 */
public class EventsPage extends BasePage {

    private static final By EVENT_CARD = By.cssSelector("[data-testid='event-card']");
    private static final By BOOK_NOW_BUTTON = By.cssSelector("[data-testid='book-now-btn']");
    // The seat-count span is always the next sibling of the price <p>; matching
    // on text() is unreliable here because the framework renders the seat
    // count and the trailing label as separate text nodes ("50" then " seats
    // available"), and XPath's contains(text(),...) only ever sees the first one.
    private static final By SEAT_TEXT = By.xpath(".//p[contains(@class,'text-indigo-700')]/following-sibling::span[1]");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

    public EventsPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToEvents() {
        navigateToPath(AppConstants.EVENTS_PATH);
    }

    public List<WebElement> getAllEventCards() {
        return findAll(EVENT_CARD);
    }

    public WebElement getFirstEventCard() {
        return getAllEventCards().get(0);
    }

    public boolean isFirstEventCardVisible() {
        return isVisible(EVENT_CARD, 10);
    }

    /** Filters the currently rendered cards for the one containing the given text. */
    public WebElement findCardByTitle(String eventTitle) {
        return getAllEventCards().stream()
                .filter(card -> card.getText().contains(eventTitle))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementFoundException("No event card found containing title: " + eventTitle));
    }

    /** Waits (up to timeoutSeconds) for a card containing eventTitle to render, then returns it. */
    public WebElement waitForCardByTitle(String eventTitle, int timeoutSeconds) {
        return WaitUtils.waitFor(driver, timeoutSeconds, d -> {
            List<WebElement> matches = getAllEventCards().stream()
                    .filter(card -> card.getText().contains(eventTitle))
                    .collect(Collectors.toList());
            return matches.isEmpty() ? null : matches.get(0);
        });
    }

    public boolean isCardVisible(WebElement card) {
        try {
            return card.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Reads the seat-count element inside a card and parses the integer value. */
    public int getSeatsRemaining(WebElement card) {
        WebElement seatElement = card.findElement(SEAT_TEXT);
        String text = seatElement.getText();
        Matcher matcher = DIGITS_PATTERN.matcher(text);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not parse seat count from text: '" + text + "'");
        }
        return Integer.parseInt(matcher.group());
    }

    public void clickBookNowOnCard(WebElement card) {
        WebElement bookNowButton = card.findElement(BOOK_NOW_BUTTON);
        click(bookNowButton);
    }

    public static class NoSuchElementFoundException extends RuntimeException {
        public NoSuchElementFoundException(String message) {
            super(message);
        }
    }
}
