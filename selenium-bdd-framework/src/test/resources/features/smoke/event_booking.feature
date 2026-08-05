@EventBooking
Feature: Event creation and booking
  As an admin I want to create a new event and as a user I want to book it
  so that the seat inventory correctly reflects each confirmed booking.

  Background:
    Given I log in to EventHub with valid credentials

  @Smoke @Critical @Regression
  Scenario: Creating a new event and booking it reduces the seat count by exactly 1
    When I navigate to the admin events page
    And I create a new event with a unique title
    Then the "Event created!" toast should be visible
    When I navigate to the events page
    And I locate the event card matching my newly created event
    Then the matched event card should be visible
    And I capture the seat count as "seatsBeforeBooking"
    When I click "Book Now" on the matched event card
    Then the ticket count field should default to "1"
    When I fill in the booking form with my details
    And I confirm the booking
    Then the booking reference should be visible
    When I click "View My Bookings"
    Then I should be on the "/bookings" page
    And I should find a booking card matching my booking reference
    And that booking card should contain my event title
    When I navigate back to the events page
    And I locate the event card matching my newly created event
    And I capture the seat count as "seatsAfterBooking"
    Then "seatsAfterBooking" should be exactly one less than "seatsBeforeBooking"
