package com.eventhub.framework.pages;

import com.eventhub.framework.base.BasePage;
import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for the booking form that appears after clicking "Book Now"
 * on an event card.
 */
public class BookingPage extends BasePage {

    private static final By TICKET_COUNT = By.id("ticket-count");
    private static final By EMAIL_INPUT = By.id("customer-email");
    private static final By PHONE_INPUT = By.cssSelector("input[placeholder='+91 98765 43210']");
    private static final By CONFIRM_BUTTON = By.cssSelector(".confirm-booking-btn");
    private static final By BOOKING_REF = By.cssSelector(".booking-ref");
    private static final By VIEW_MY_BOOKINGS_LINK = By.partialLinkText("View My Bookings");
    // The stepper +/- buttons have no id/data-testid; the increment button is
    // the element immediately following the #ticket-count span in the DOM.
    private static final By INCREASE_TICKET_COUNT_BUTTON = By.xpath("//span[@id='ticket-count']/following-sibling::button[1]");

    private static final String FULL_NAME_LABEL = "Full Name";

    public BookingPage(WebDriver driver) {
        super(driver);
    }

    public String getTicketCountText() {
        return getText(TICKET_COUNT);
    }

    /**
     * Increases the ticket quantity from its default of 1 up to {@code desiredCount}
     * by repeatedly clicking the increment stepper button.
     */
    public void setTicketCount(int desiredCount) {
        int currentCount = Integer.parseInt(getTicketCountText().trim());
        while (currentCount < desiredCount) {
            click(INCREASE_TICKET_COUNT_BUTTON);
            currentCount = Integer.parseInt(getTicketCountText().trim());
        }
    }

    public void enterFullName(String fullName) {
        typeByLabel(FULL_NAME_LABEL, fullName);
    }

    public void enterEmail(String email) {
        type(EMAIL_INPUT, email);
    }

    public void enterPhone(String phone) {
        type(PHONE_INPUT, phone);
    }

    public void fillBookingForm(String fullName, String email, String phone) {
        enterFullName(fullName);
        enterEmail(email);
        enterPhone(phone);
    }

    public void clickConfirmBooking() {
        click(CONFIRM_BUTTON);
    }

    public boolean isBookingRefVisible() {
        return isVisible(BOOKING_REF, 10);
    }

    public String getBookingReference() {
        WebElement firstBookingRef = WaitUtils.waitForAllVisible(driver, BOOKING_REF).get(0);
        return getText(firstBookingRef).trim();
    }

    public void clickViewMyBookings() {
        click(VIEW_MY_BOOKINGS_LINK);
    }
}
