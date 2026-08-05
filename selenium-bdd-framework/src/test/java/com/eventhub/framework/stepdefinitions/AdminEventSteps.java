package com.eventhub.framework.stepdefinitions;

import com.eventhub.framework.context.ScenarioContext;
import com.eventhub.framework.constants.AppConstants;
import com.eventhub.framework.constants.TestDataConstants;
import com.eventhub.framework.factory.DriverManager;
import com.eventhub.framework.pages.AdminEventPage;
import com.eventhub.framework.utilities.DateTimeUtils;
import com.eventhub.framework.utilities.JsonUtils;
import com.eventhub.framework.utilities.LogUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import java.util.Map;

/** Step definitions for the admin "create event" flow (/admin/events). */
public class AdminEventSteps {

    private static final Logger log = LogUtils.getLogger(AdminEventSteps.class);

    private final ScenarioContext scenarioContext;

    public AdminEventSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    private AdminEventPage adminEventPage() {
        return new AdminEventPage(driver());
    }

    @When("I navigate to the admin events page")
    public void i_navigate_to_the_admin_events_page() {
        adminEventPage().navigateToAdminEvents();
    }

    @When("I create a new event with a unique title")
    public void i_create_a_new_event_with_a_unique_title() {
        @SuppressWarnings("unchecked")
        Map<String, Object> eventData = (Map<String, Object>) JsonUtils
                .readAsMap(AppConstants.TEST_DATA_DIR + TestDataConstants.EVENT_DATA_FILE)
                .get(TestDataConstants.DEFAULT_EVENT_PROFILE);

        String eventTitle = eventData.get("titlePrefix") + " " + DateTimeUtils.currentEpochMillis();
        int futureDaysAhead = ((Number) eventData.getOrDefault("futureDaysAhead", 7)).intValue();
        String eventDateTimeValue = DateTimeUtils.futureDateValue(futureDaysAhead);

        log.info("Creating new event with title: {}", eventTitle);
        adminEventPage().createEvent(
                eventTitle,
                (String) eventData.get("description"),
                (String) eventData.get("city"),
                (String) eventData.get("venue"),
                eventDateTimeValue,
                (String) eventData.get("price"),
                (String) eventData.get("totalSeats"));

        scenarioContext.set(ScenarioContext.EVENT_TITLE, eventTitle);
    }

    @Then("the {string} toast should be visible")
    public void the_toast_should_be_visible(String toastMessage) {
        Assertions.assertTrue(adminEventPage().isEventCreatedToastVisible(),
                "Expected toast message not visible: " + toastMessage);
    }
}
