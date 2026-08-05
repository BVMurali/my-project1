package com.eventhub.framework.stepdefinitions;

import com.eventhub.framework.config.ConfigManager;
import com.eventhub.framework.factory.DriverManager;
import com.eventhub.framework.helpers.AuthenticationHelper;
import com.eventhub.framework.pages.BookingDetailPage;
import com.eventhub.framework.pages.BookingPage;
import com.eventhub.framework.pages.EventsPage;
import com.eventhub.framework.pages.LoginPage;
import com.eventhub.framework.utilities.LogUtils;
import com.eventhub.framework.utilities.WaitUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

/**
 * Cross-cutting step definitions shared by every feature: login, generic
 * page navigation/assertions, and generic click-by-label dispatch for
 * link/button text that isn't specific to any single page object.
 */
public class CommonSteps {

    private static final Logger log = LogUtils.getLogger(CommonSteps.class);

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @Given("I log in to EventHub with valid credentials")
    public void i_log_in_to_event_hub_with_valid_credentials() {
        new AuthenticationHelper(driver()).loginAndVerify();
    }

    @Then("the {string} link should be visible")
    public void the_link_should_be_visible(String linkText) {
        Assertions.assertTrue(new LoginPage(driver()).isBrowseEventsLinkVisible(),
                "Expected link not visible: " + linkText);
    }

    @When("I navigate to the events page")
    public void i_navigate_to_the_events_page() {
        new EventsPage(driver()).navigateToEvents();
    }

    @When("I navigate back to the events page")
    public void i_navigate_back_to_the_events_page() {
        new EventsPage(driver()).navigateToEvents();
    }

    @Then("the first event card should be visible")
    public void the_first_event_card_should_be_visible() {
        Assertions.assertTrue(new EventsPage(driver()).isFirstEventCardVisible(),
                "Expected at least one event card to be visible");
    }

    @Then("I should be on the {string} page")
    public void i_should_be_on_the_page(String path) {
        String expectedUrl = ConfigManager.getInstance().getBaseUrl() + path;
        Assertions.assertTrue(WaitUtils.waitForUrlToBe(driver(), expectedUrl),
                "Expected to be on page: " + expectedUrl + " but was on: " + driver().getCurrentUrl());
    }

    @When("I click {string}")
    public void i_click(String label) {
        switch (label) {
            case "View My Bookings" -> new BookingPage(driver()).clickViewMyBookings();
            case "Check Refund Eligibility" -> new BookingDetailPage(driver()).clickCheckRefundEligibility();
            default -> throw new IllegalArgumentException("Unsupported click target: " + label);
        }
    }
}
