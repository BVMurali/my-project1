package com.eventhub.framework.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Generic utility for loading .properties files from either the filesystem
 * (project-relative path) or the classpath (packaged inside resources).
 */
public final class PropertyReaderUtils {

    private PropertyReaderUtils() {
    }

    public static Properties loadFromFile(String filePath) {
        Properties properties = new Properties();
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new RuntimeException("Property file not found at path: " + filePath);
        }
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load property file: " + filePath, e);
        }
        return properties;
    }

    public static Properties loadFromClasspath(String resourceName) {
        Properties properties = new Properties();
        try (InputStream is = PropertyReaderUtils.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new RuntimeException("Property file not found on classpath: " + resourceName);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load property file from classpath: " + resourceName, e);
        }
        return properties;
    }
}
