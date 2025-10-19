package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;

import io.cucumber.java.Scenario;
import org.com.driver.DriverManager;
import org.com.util.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before(value = "@ui", order = 0)
    public void startDriver() {
        WebDriver driver = DriverManager.getDriver();
        logger.info("WebDriver started: {}", driver);
    }

    @After(order = 1)
    public void attachScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed() && DriverManager.hasDriver()) {
            byte[] bytes = ScreenshotUtil.takeScreenshotBytes();
            if (bytes.length > 0) {
                scenario.attach(bytes, "image/png", "screenshot");
                ScreenshotUtil.saveScreenshot(bytes, scenario.getName());
            } else {
                logger.warn("No screenshot bytes captured for failed scenario: {}", scenario.getName());
            }
        }
    }

    @After(value = "@ui", order = 0)
    public void quitDriver() {
        logger.info("Quitting WebDriver");
        DriverManager.quitDriver();
    }
}


