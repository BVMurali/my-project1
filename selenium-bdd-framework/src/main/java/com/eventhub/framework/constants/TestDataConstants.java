package com.eventhub.framework.constants;

/**
 * Keys used to look up entries inside the JSON test data files under
 * src/main/resources/testdata/.
 */
public final class TestDataConstants {

    private TestDataConstants() {
    }

    public static final String EVENT_DATA_FILE = "eventTestData.json";
    public static final String BOOKING_DATA_FILE = "bookingTestData.json";

    public static final String DEFAULT_EVENT_PROFILE = "defaultEvent";
    public static final String DEFAULT_BOOKING_PROFILE = "defaultBooking";
}
