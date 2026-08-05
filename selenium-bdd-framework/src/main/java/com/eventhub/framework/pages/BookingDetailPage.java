package com.eventhub.framework.pages;

import com.eventhub.framework.base.BasePage;
import com.eventhub.framework.utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the booking detail page reached via "View Details" from
 * My Bookings, including the refund-eligibility check widget.
 */
public class BookingDetailPage extends BasePage {

    private static final By BOOKING_INFORMATION_HEADING = By.xpath("//*[contains(text(),'Booking Information')]");
    // Unlike the booking confirmation page, the detail page does not reuse
    // the ".booking-ref" class - the ref is shown in a font-mono/font-bold
    // badge next to the status pill (the breadcrumb above it is also
    // font-mono but NOT font-bold, so this combination stays unambiguous).
    private static final By BOOKING_REF = By.cssSelector("span.font-mono.font-bold");
    private static final By EVENT_TITLE_HEADING = By.tagName("h1");
    // The visible button text is "Check eligibility for refund?", not
    // "Check Refund Eligibility" - id is the stable contract here.
    private static final By CHECK_REFUND_ELIGIBILITY_BUTTON = By.id("check-refund-btn");
    private static final By REFUND_SPINNER = By.id("refund-spinner");
    private static final By REFUND_RESULT = By.id("refund-result");

    public BookingDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isBookingInformationVisible() {
        return isVisible(BOOKING_INFORMATION_HEADING, 10);
    }

    public String getBookingReference() {
        return getText(BOOKING_REF).trim();
    }

    public String getEventTitle() {
        return getText(EVENT_TITLE_HEADING).trim();
    }

    public void clickCheckRefundEligibility() {
        click(CHECK_REFUND_ELIGIBILITY_BUTTON);
    }

    public boolean isSpinnerVisible() {
        return isVisible(REFUND_SPINNER, 3);
    }

    public boolean waitForSpinnerToDisappear(int timeoutSeconds) {
        return isInvisible(REFUND_SPINNER, timeoutSeconds);
    }

    public boolean isRefundResultVisible() {
        return isVisible(REFUND_RESULT, 10);
    }

    public String getRefundResultText() {
        return getText(REFUND_RESULT);
    }
}
