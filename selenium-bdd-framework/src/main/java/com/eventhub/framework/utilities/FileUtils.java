package com.eventhub.framework.utilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Generic filesystem helpers: reading/writing text, ensuring directories
 * exist, and cleaning up stale artifacts between runs.
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static String readFileAsString(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    public static List<String> readLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read lines from file: " + path, e);
        }
    }

    public static void writeStringToFile(String path, String content) {
        try {
            Path target = Paths.get(path);
            ensureParentDirectoryExists(target);
            Files.writeString(target, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + path, e);
        }
    }

    public static void ensureDirectoryExists(String directoryPath) {
        try {
            Files.createDirectories(Paths.get(directoryPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }

    private static void ensureParentDirectoryExists(Path target) throws IOException {
        if (target.getParent() != null) {
            Files.createDirectories(target.getParent());
        }
    }

    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
}
