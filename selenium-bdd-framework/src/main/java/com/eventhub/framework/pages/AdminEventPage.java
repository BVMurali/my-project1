
package com.eventhub.framework.pages;

import com.eventhub.framework.base.BasePage;
import com.eventhub.framework.constants.AppConstants;
import com.eventhub.framework.utilities.JSUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for the admin "Create Event" screen (/admin/events).
 */
public class AdminEventPage extends BasePage {

    private static final By TITLE_INPUT = By.id("event-title-input");
    private static final By DESCRIPTION_TEXTAREA = By.cssSelector("#admin-event-form textarea");
    private static final By SUBMIT_BUTTON = By.id("add-event-btn");
    private static final By EVENT_CREATED_TOAST = By.xpath("//*[contains(text(),'Event created!')]");

    private static final String CITY_LABEL = "City";
    private static final String VENUE_LABEL = "Venue";
    private static final String EVENT_DATE_TIME_LABEL = "Event Date & Time";
    private static final String PRICE_LABEL = "Price ($)";
    private static final String TOTAL_SEATS_LABEL = "Total Seats";

    public AdminEventPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToAdminEvents() {
        navigateToPath(AppConstants.ADMIN_EVENTS_PATH);
    }

    public void enterTitle(String title) {
        type(TITLE_INPUT, title);
    }

    public void enterDescription(String description) {
        type(DESCRIPTION_TEXTAREA, description);
    }

    public void enterCity(String city) {
        typeByLabel(CITY_LABEL, city);
    }

    public void enterVenue(String venue) {
        typeByLabel(VENUE_LABEL, venue);
    }

    /**
     * The "Event Date & Time" field is a native segmented
     * &lt;input type="datetime-local"&gt;. Typing an ISO string into it via
     * sendKeys() gets misinterpreted by the browser's per-segment
     * auto-advance, so the value is set directly via JS instead.
     */
    public void enterEventDateTime(String dateTimeValue) {
        WebElement dateTimeInput = findByLabel(EVENT_DATE_TIME_LABEL);
        JSUtils.setControlledInputValue(driver, dateTimeInput, dateTimeValue);
    }

    public void enterPrice(String price) {
        typeByLabel(PRICE_LABEL, price);
    }

    public void enterTotalSeats(String totalSeats) {
        typeByLabel(TOTAL_SEATS_LABEL, totalSeats);
    }

    /** Fills out the entire create-event form in one call. */
    public void createEvent(String title, String description, String city, String venue,
                             String eventDateTimeValue, String price, String totalSeats) {
        enterTitle(title);
        enterDescription(description);
        enterCity(city);
        enterVenue(venue);
        enterEventDateTime(eventDateTimeValue);
        enterPrice(price);
        enterTotalSeats(totalSeats);
        submit();
    }

    public void submit() {
        click(SUBMIT_BUTTON);
    }

    public boolean isEventCreatedToastVisible() {
        return isVisible(EVENT_CREATED_TOAST, 10);
    }
}
