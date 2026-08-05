@RefundEligibility
Feature: Booking refund eligibility
  As a customer I want to check whether my booking qualifies for a refund
  so that I know if I can get my money back before requesting one.

  Background:
    Given I log in to EventHub with valid credentials

  @Regression @Critical
  Scenario: A single-ticket booking is eligible for a full refund
    When I navigate to the events page
    And I click "Book Now" on the first event card
    And I fill in the booking form with my details
    And I confirm the booking
    And I click "View My Bookings"
    Then I should be on the "/bookings" page
    When I click the first "View Details" link
    Then "Booking Information" should be visible on the page
    And the first character of the booking reference should equal the first character of the event title
    When I click "Check Refund Eligibility"
    Then the refund spinner should be visible immediately
    And the refund spinner should disappear within 6 seconds
    And the refund result should be visible
    And the refund result should contain "Eligible for refund"
    And the refund result should contain "Single-ticket bookings qualify for a full refund"

  @Regression
  Scenario: A three-ticket booking is not eligible for a refund
    When I navigate to the events page
    And I click "Book Now" on the first event card
    And I set the ticket count to 3
    And I fill in the booking form with my details
    And I confirm the booking
    And I click "View My Bookings"
    Then I should be on the "/bookings" page
    When I click the first "View Details" link
    Then "Booking Information" should be visible on the page
    And the first character of the booking reference should equal the first character of the event title
    When I click "Check Refund Eligibility"
    Then the refund spinner should be visible immediately
    And the refund spinner should disappear within 6 seconds
    And the refund result should be visible
    And the refund result should contain "Not eligible for refund"
