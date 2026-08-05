package com.eventhub.framework.stepdefinitions;

import com.eventhub.framework.constants.AppConstants;
import com.eventhub.framework.constants.TestDataConstants;
import com.eventhub.framework.context.ScenarioContext;
import com.eventhub.framework.factory.DriverManager;
import com.eventhub.framework.pages.BookingPage;
import com.eventhub.framework.pages.EventsPage;
import com.eventhub.framework.pages.MyBookingsPage;
import com.eventhub.framework.utilities.JsonUtils;
import com.eventhub.framework.utilities.LogUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Step definitions covering the /events listing, per-card booking actions,
 * the booking form, and the seat-count verification that ties the whole
 * event-booking flow together.
 */
public class EventBookingSteps {

    private static final Logger log = LogUtils.getLogger(EventBookingSteps.class);
    private static final String MATCHED_EVENT_CARD = "matchedEventCard";
    private static final String MATCHED_BOOKING_CARD = "matchedBookingCard";

    private final ScenarioContext scenarioContext;

    public EventBookingSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    private EventsPage eventsPage() {
        return new EventsPage(driver());
    }

    private BookingPage bookingPage() {
        return new BookingPage(driver());
    }

    private MyBookingsPage myBookingsPage() {
        return new MyBookingsPage(driver());
    }

    @When("I locate the event card matching my newly created event")
    public void i_locate_the_event_card_matching_my_newly_created_event() {
        String eventTitle = scenarioContext.getString(ScenarioContext.EVENT_TITLE);
        WebElement card = eventsPage().waitForCardByTitle(eventTitle, 5);
        scenarioContext.set(MATCHED_EVENT_CARD, card);
    }

    @Then("the matched event card should be visible")
    public void the_matched_event_card_should_be_visible() {
        WebElement card = (WebElement) scenarioContext.get(MATCHED_EVENT_CARD);
        Assertions.assertTrue(eventsPage().isCardVisible(card), "Matched event card is not visible");
    }

    @And("I capture the seat count as {string}")
    public void i_capture_the_seat_count_as(String contextKey) {
        WebElement card = (WebElement) scenarioContext.get(MATCHED_EVENT_CARD);
        int seats = eventsPage().getSeatsRemaining(card);
        log.info("Captured seat count '{}' = {}", contextKey, seats);
        scenarioContext.set(contextKey, seats);
    }

    @When("I click {string} on the matched event card")
    public void i_click_on_the_matched_event_card(String buttonLabel) {
        WebElement card = (WebElement) scenarioContext.get(MATCHED_EVENT_CARD);
        eventsPage().clickBookNowOnCard(card);
    }

    @When("I click {string} on the first event card")
    public void i_click_on_the_first_event_card(String buttonLabel) {
        WebElement card = eventsPage().getFirstEventCard();
        eventsPage().clickBookNowOnCard(card);
    }

    @Then("the ticket count field should default to {string}")
    public void the_ticket_count_field_should_default_to(String expectedCount) {
        Assertions.assertEquals(expectedCount, bookingPage().getTicketCountText().trim(),
                "Default ticket count did not match");
    }

    @When("I fill in the booking form with my details")
    public void i_fill_in_the_booking_form_with_my_details() {
        @SuppressWarnings("unchecked")
        Map<String, Object> bookingData = (Map<String, Object>) JsonUtils
                .readAsMap(AppConstants.TEST_DATA_DIR + TestDataConstants.BOOKING_DATA_FILE)
                .get(TestDataConstants.DEFAULT_BOOKING_PROFILE);

        bookingPage().fillBookingForm(
                (String) bookingData.get("fullName"),
                (String) bookingData.get("email"),
                (String) bookingData.get("phone"));
    }

    @When("I set the ticket count to {int}")
    public void i_set_the_ticket_count_to(int desiredCount) {
        bookingPage().setTicketCount(desiredCount);
    }

    @When("I confirm the booking")
    public void i_confirm_the_booking() {
        bookingPage().clickConfirmBooking();
    }

    @Then("the booking reference should be visible")
    public void the_booking_reference_should_be_visible() {
        Assertions.assertTrue(bookingPage().isBookingRefVisible(), "Booking reference is not visible");
        String bookingRef = bookingPage().getBookingReference();
        log.info("Captured booking reference: {}", bookingRef);
        scenarioContext.set(ScenarioContext.BOOKING_REFERENCE, bookingRef);
    }

    @And("I should find a booking card matching my booking reference")
    public void i_should_find_a_booking_card_matching_my_booking_reference() {
        String bookingRef = scenarioContext.getString(ScenarioContext.BOOKING_REFERENCE);
        WebElement card = myBookingsPage().findCardByBookingRef(bookingRef);
        Assertions.assertTrue(card.isDisplayed(), "Matched booking card is not visible");
        scenarioContext.set(MATCHED_BOOKING_CARD, card);
    }

    @And("that booking card should contain my event title")
    public void that_booking_card_should_contain_my_event_title() {
        WebElement card = (WebElement) scenarioContext.get(MATCHED_BOOKING_CARD);
        String eventTitle = scenarioContext.getString(ScenarioContext.EVENT_TITLE);
        Assertions.assertTrue(myBookingsPage().cardContainsText(card, eventTitle),
                "Booking card does not contain event title: " + eventTitle);
    }

    @Then("{string} should be exactly one less than {string}")
    public void should_be_exactly_one_less_than(String afterKey, String beforeKey) {
        int before = scenarioContext.getInt(beforeKey);
        int after = scenarioContext.getInt(afterKey);
        Assertions.assertEquals(before - 1, after,
                String.format("Expected %s (%d) to be exactly one less than %s (%d)", afterKey, after, beforeKey, before));
    }
}
