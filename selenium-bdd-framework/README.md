# EventHub Selenium BDD Automation Framework

Enterprise-grade Selenium WebDriver + Cucumber BDD automation framework for
[EventHub](https://eventhub.rahulshettyacademy.com), built with Java, Maven,
JUnit 5, and the Page Object Model.

## Tech stack

| Concern         | Tool                                                    |
|-----------------|----------------------------------------------------------|
| Language        | Java 17                                                  |
| Browser driver  | Selenium WebDriver 4.x + WebDriverManager (auto binaries) |
| BDD             | Cucumber JVM 7 (Gherkin)                                  |
| Design pattern  | Page Object Model                                        |
| Build tool      | Maven                                                     |
| Test runner     | JUnit 5 Platform Suite                                    |
| Logging         | Log4j2 / SLF4J                                            |
| Reporting       | Allure, Extent (Spark), Cucumber HTML/JSON                |

## What is covered

1. **Event creation & booking** (`features/smoke/event_booking.feature`)
   Creates a new event from the admin panel, books it as a user, and asserts
   the seat count drops by exactly 1.
2. **Refund eligibility** (`features/regression/refund_eligibility.feature`)
   Books an event with 1 ticket (expects "Eligible for refund") and with 3
   tickets (expects "Not eligible for refund"), asserting the refund spinner
   appears and then disappears before the result is shown.
3. **Sanity checks** (`features/sanity/login_sanity.feature`)
   Login and events-listing smoke checks.

> **Note on the first-event-card scenarios:** `refund_eligibility.feature`
> books "the very first event card" per spec. In this account's sandbox that
> card ("Dilli Diwali Mela") started with only 8 seats and got exhausted to
> 0 by repeated runs during development/testing, which disables its "+"
> stepper and confirm button (Selenium correctly times out waiting for a
> disabled control to become clickable - this is expected behavior, not a
> framework bug). If you see that failure, either wait for inventory to free
> up, book against a different card, or extend `EventsPage`/`BookingPage`
> with a "has available seats" guard before booking.

## Project structure

```
selenium-bdd-framework/
├── src/main/java/com/eventhub/framework/
│   ├── base/            BasePage - shared Page Object superclass
│   ├── config/          ConfigManager - merges config.properties + env + -D overrides
│   ├── constants/       AppConstants, TestDataConstants
│   ├── pages/           LoginPage, AdminEventPage, EventsPage, BookingPage,
│   │                    MyBookingsPage, BookingDetailPage
│   ├── helpers/         UIActionsHelper, NavigationHelper, AuthenticationHelper,
│   │                    AlertHelper, DropdownHelper, FrameHelper, WindowHelper, ApiHelper
│   ├── utilities/       WaitUtils, JSUtils, ScreenshotUtils, LogUtils, DateTimeUtils,
│   │                    FileUtils, ExcelUtils, JsonUtils, PropertyReaderUtils, EnvironmentUtils
│   └── factory/         DriverManager (ThreadLocal), BrowserFactory
├── src/main/resources/
│   ├── config.properties
│   ├── environments/    qa.properties, dev.properties, staging.properties
│   ├── log4j2.xml
│   └── testdata/        eventTestData.json, bookingTestData.json
├── src/test/java/com/eventhub/framework/
│   ├── context/         ScenarioContext (per-scenario shared state, DI'd via PicoContainer)
│   ├── hooks/           Hooks (driver lifecycle, failure screenshots)
│   ├── stepdefinitions/ CommonSteps, AdminEventSteps, EventBookingSteps, RefundEligibilitySteps
│   └── runners/         SmokeTestRunner, RegressionTestRunner, SanityTestRunner
└── src/test/resources/
    ├── features/{smoke,regression,sanity}/*.feature
    ├── junit-platform.properties
    ├── allure.properties
    ├── extent.properties / extent-config.xml
```

## Prerequisites

- JDK 17+
- Maven 3.8+
- Chrome, Firefox, or Edge installed locally (WebDriverManager downloads the
  matching driver binary automatically - no manual setup needed)

## Configuration

Defaults live in `src/main/resources/config.properties`, merged with an
environment file from `src/main/resources/environments/` (selected via
`-Denv=qa|dev|staging`, default `qa`). Every property can be overridden
from the command line, e.g. `-Dbrowser=firefox -Dheadless=true`.

Key properties:

| Property           | Default | Description                          |
|--------------------|---------|---------------------------------------|
| `browser`          | chrome  | chrome \| firefox \| edge             |
| `headless`         | false   | run browser headless                  |
| `env`              | qa      | selects `environments/<env>.properties` |
| `implicit.wait`    | 5       | seconds                               |
| `explicit.wait`    | 15      | seconds                               |
| `page.load.timeout`| 30      | seconds                               |

Base URL and test credentials live in `environments/qa.properties`.

## Running the tests

```bash
# Run everything (all three runners)
mvn test

# Run a specific suite
mvn test -Dtest=SmokeTestRunner
mvn test -Dtest=RegressionTestRunner
mvn test -Dtest=SanityTestRunner

# Same, via Maven profiles (also usable to combine with browser/env profiles)
mvn test -Psmoke
mvn test -Pregression
mvn test -Psanity

# Choose a browser / environment
mvn test -Dtest=SmokeTestRunner -Dbrowser=firefox
mvn test -Dtest=RegressionTestRunner -Denv=staging -Dheadless=true
```

Suites are also selectable by Cucumber tag directly:
`@Smoke`, `@Regression`, `@Sanity`, `@Critical` (see feature files).

### Parallel execution

Cucumber's JUnit Platform engine runs scenarios in parallel by
configuration in `src/test/resources/junit-platform.properties`
(`cucumber.execution.parallel.enabled=true`, dynamic strategy). Each thread
gets its own `WebDriver` instance via `DriverManager`'s `ThreadLocal`, so
scenarios never share a browser session.

## Reports

After a run, open:

- `reports/cucumber-html-report.html` - Cucumber HTML report
- `reports/cucumber-report.json` - Cucumber JSON (for CI dashboards)
- `reports/extent/ExtentSparkReport.html` - Extent report
- `allure-results/` - raw Allure results; generate the HTML report with:
  ```bash
  mvn allure:report      # writes to allure-report/
  mvn allure:serve       # builds and opens it in a browser
  ```
- `screenshots/` - captured automatically for every failed scenario and
  attached to the Cucumber/Allure/Extent reports
- `logs/automation.log` - full execution log (Log4j2, rolling daily)

## Test data

- `src/main/resources/testdata/eventTestData.json` - the event profile used
  when creating a new event (title is generated at runtime as
  `Test Event <epoch millis>` for uniqueness).
- `src/main/resources/testdata/bookingTestData.json` - booker
  name/email/phone, plus the expected refund-eligibility text per ticket count.

## Extending the framework

- **New page:** add a class under `pages/` extending `BasePage`; keep only
  locators and page actions there - no assertions.
- **New flow:** add a `.feature` file under the right suite folder
  (`smoke`/`regression`/`sanity`), then implement any new step text in the
  existing step definition classes (or a new one) under `stepdefinitions/`.
- **New environment:** add `environments/<name>.properties` and run with
  `-Denv=<name>`.
- **CI/CD:** the Maven Surefire + Allure/Extent/Cucumber plugin wiring in
  `pom.xml` and `junit-platform.properties` works unchanged under Jenkins,
  GitHub Actions, or Azure DevOps - just publish the `reports/`,
  `allure-results/`, and `screenshots/` directories as build artifacts.

## Verification note

This framework was executed live against
`https://eventhub.rahulshettyacademy.com` with a real Microsoft Edge browser
(WebDriverManager's download CDN was unreachable from this network, so
`msedgedriver` was pre-provisioned and passed via `-Dwebdriver.edge.driver`
- see `BrowserFactory.setupManagedDriverUnlessPreProvisioned`, a reusable
fallback for any locked-down CI runner).

Confirmed passing end-to-end on the live site:
- **Smoke**: create event → book it → seat count drops from 50 to 49 exactly.
- **Regression**: 3-ticket booking → spinner shows/hides → "Not eligible for
  refund. Group bookings (3 tickets) are non-refundable."
- **Sanity**: both scenarios.

The 1-ticket refund scenario is currently blocked in this account's sandbox
by seat exhaustion on the hardcoded "first event card" (see the note above),
not by a framework defect - it was passing before that card sold out.

Live execution surfaced and fixed four real bugs that a dry-run / compile
check could not have caught (all in `pages/`):
1. The "Event Date & Time" field is a native segmented
   `<input type="datetime-local">`; `sendKeys()` with an ISO string got
   misinterpreted digit-by-digit. Fixed by setting the value via JS
   (`JSUtils.setControlledInputValue`) and dispatching input/change events.
2. The seat-count text ("50 seats available") is split across two separate
   DOM text nodes by the frontend, so XPath's `contains(text(),...)` only
   ever saw the first fragment. Fixed with a structural locator (the span
   following the price `<p>`) instead of matching on text.
3. The ticket-count "+" stepper has no id/data-testid; fixed by locating it
   as the sibling immediately after `#ticket-count`.
4. The booking detail page doesn't reuse the `.booking-ref` class from the
   confirmation modal, and the refund button's visible text
   ("Check eligibility for refund?") doesn't match "Check Refund
   Eligibility". Fixed with `span.font-mono.font-bold` and `#check-refund-btn`
   respectively.
