package com.eventhub.framework.context;

import java.util.HashMap;
import java.util.Map;


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
