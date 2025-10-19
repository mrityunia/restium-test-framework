package org.com.util;

import org.com.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {}

    public static byte[] takeScreenshotBytes() {
        WebDriver driver = DriverManager.getExistingDriver();
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        logger.warn("Driver does not support screenshots: {}", driver);
        return new byte[0];
    }

    public static File saveScreenshot(byte[] data, String scenarioName) {
        if (data == null || data.length == 0) {
            return null;
        }
        String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
        String timestamp = LocalDateTime.now().format(TS);
        Path dir = Paths.get("target", "screenshots");
        try {
            Files.createDirectories(dir);
            Path file = dir.resolve(safeName + "_" + timestamp + ".png");
            try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
                fos.write(data);
            }
            logger.info("Saved screenshot: {}", file.toAbsolutePath());
            return file.toFile();
        } catch (IOException e) {
            logger.error("Failed to save screenshot", e);
            return null;
        }
    }
}


