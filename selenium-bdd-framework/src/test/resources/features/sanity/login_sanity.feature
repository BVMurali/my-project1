@LoginSanity
Feature: Application sanity checks
  Basic checks that confirm the application is up and the core
  authenticated shell renders correctly, before running deeper suites.

  @Sanity
  Scenario: A user can log in and land on an authenticated view
    Given I log in to EventHub with valid credentials
    Then the "Browse Events ->" link should be visible

  @Sanity
  Scenario: The events listing page renders at least one event
    Given I log in to EventHub with valid credentials
    When I navigate to the events page
    Then the first event card should be visible
