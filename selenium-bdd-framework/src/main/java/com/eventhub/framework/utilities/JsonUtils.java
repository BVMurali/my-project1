package com.eventhub.framework.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Reads JSON test data files (src/main/resources/testdata/*.json) into
 * generic Maps or strongly-typed objects via Jackson.
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T fromJsonFile(String filePath, Class<T> type) {
        try {
            return MAPPER.readValue(new File(filePath), type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON file: " + filePath, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> readAsMap(String filePath) {
        return fromJsonFile(filePath, Map.class);
    }

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }
}
