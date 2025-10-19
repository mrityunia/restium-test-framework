package org.com.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigLoader() {}

    public static String get(String key) {
        String systemOverride = System.getProperty(key);
        if (systemOverride != null && !systemOverride.isBlank()) {
            return systemOverride;
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing configuration for key: " + key);
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}


