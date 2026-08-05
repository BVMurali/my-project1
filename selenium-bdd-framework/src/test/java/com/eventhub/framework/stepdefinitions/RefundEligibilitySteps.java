package com.eventhub.framework.stepdefinitions;

import com.eventhub.framework.factory.DriverManager;
import com.eventhub.framework.pages.BookingDetailPage;
import com.eventhub.framework.pages.MyBookingsPage;
import com.eventhub.framework.utilities.LogUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

/**
 * Step definitions for the booking detail page and its refund-eligibility
 * check widget (spinner + result).
 */
public class RefundEligibilitySteps {

    private static final Logger log = LogUtils.getLogger(RefundEligibilitySteps.class);

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    private MyBookingsPage myBookingsPage() {
        return new MyBookingsPage(driver());
    }

    private BookingDetailPage bookingDetailPage() {
        return new BookingDetailPage(driver());
    }

    @When("I click the first {string} link")
    public void i_click_the_first_link(String linkText) {
        myBookingsPage().clickFirstViewDetails();
    }

    @Then("{string} should be visible on the page")
    public void should_be_visible_on_the_page(String expectedText) {
        Assertions.assertTrue(bookingDetailPage().isBookingInformationVisible(),
                "Expected text not visible on page: " + expectedText);
    }

    @Then("the first character of the booking reference should equal the first character of the event title")
    public void the_first_character_of_the_booking_reference_should_equal_the_first_character_of_the_event_title() {
        String bookingRef = bookingDetailPage().getBookingReference();
        String eventTitle = bookingDetailPage().getEventTitle();
        log.info("Comparing first characters - bookingRef='{}' eventTitle='{}'", bookingRef, eventTitle);
        Assertions.assertEquals(eventTitle.charAt(0), bookingRef.charAt(0),
                "First character of booking reference does not match first character of event title");
    }

    @Then("the refund spinner should be visible immediately")
    public void the_refund_spinner_should_be_visible_immediately() {
        Assertions.assertTrue(bookingDetailPage().isSpinnerVisible(), "Refund spinner was not visible immediately");
    }

    @Then("the refund spinner should disappear within {int} seconds")
    public void the_refund_spinner_should_disappear_within_seconds(int timeoutSeconds) {
        Assertions.assertTrue(bookingDetailPage().waitForSpinnerToDisappear(timeoutSeconds),
                "Refund spinner did not disappear within " + timeoutSeconds + " seconds");
    }

    @Then("the refund result should be visible")
    public void the_refund_result_should_be_visible() {
        Assertions.assertTrue(bookingDetailPage().isRefundResultVisible(), "Refund result is not visible");
    }

    @Then("the refund result should contain {string}")
    public void the_refund_result_should_contain(String expectedText) {
        String actualText = bookingDetailPage().getRefundResultText();
        Assertions.assertTrue(actualText.contains(expectedText),
                "Expected refund result to contain '" + expectedText + "' but was: '" + actualText + "'");
    }
}
