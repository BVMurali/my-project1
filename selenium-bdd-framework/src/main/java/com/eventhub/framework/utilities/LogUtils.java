package com.eventhub.framework.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thin wrapper around SLF4J so every class in the framework logs through a
 * single, consistent entry point (backed by log4j2.xml).
 */
public final class LogUtils {

    private LogUtils() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
