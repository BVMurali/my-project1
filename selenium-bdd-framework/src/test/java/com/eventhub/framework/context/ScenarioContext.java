package com.eventhub.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-scenario shared state, injected via Cucumber-PicoContainer into every
 * step definition class that declares it as a constructor dependency. This
 * is how data captured in one step (e.g. the generated event title, the
 * seat count before booking, the booking reference) flows to later steps
 * without step definition classes reaching into each other directly.
 */
public class ScenarioContext {

    public static final String EVENT_TITLE = "eventTitle";
    public static final String SEATS_BEFORE_BOOKING = "seatsBeforeBooking";
    public static final String SEATS_AFTER_BOOKING = "seatsAfterBooking";
    public static final String BOOKING_REFERENCE = "bookingRef";

    private final Map<String, Object> store = new HashMap<>();

    public void set(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public String getString(String key) {
        Object value = store.get(key);
        return value == null ? null : value.toString();
    }

    public int getInt(String key) {
        Object value = store.get(key);
        if (value == null) {
            throw new IllegalStateException("No value stored in ScenarioContext for key: " + key);
        }
        return (Integer) value;
    }

    public boolean has(String key) {
        return store.containsKey(key);
    }

    public void clear() {
        store.clear();
    }
}
