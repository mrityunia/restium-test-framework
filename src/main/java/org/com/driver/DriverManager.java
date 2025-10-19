package org.com.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.com.config.ConfigLoader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        WebDriver driver = threadLocalDriver.get();
        if (driver == null) {
            driver = createDriver();
            threadLocalDriver.set(driver);
        }
        return driver;
    }

    public static WebDriver getExistingDriver() {
        return threadLocalDriver.get();
    }

    public static boolean hasDriver() {
        return threadLocalDriver.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            driver.quit();
            threadLocalDriver.remove();
        }
    }

    private static WebDriver createDriver() {
        String browser = System.getProperty("browser", ConfigLoader.get("browser"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(ConfigLoader.getBoolean("headless"))));
        switch (browser.toLowerCase()) {
            case "chrome": {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--hide-scrollbars");
                    options.addArguments("--disable-blink-features=AutomationControlled");
                    options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36");
                }
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--force-device-scale-factor=1");
                options.addArguments("--disable-features=VizDisplayCompositor");
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36");
                String chromeBinary = System.getProperty("chromeBinary");
                if (chromeBinary != null && !chromeBinary.isBlank()) {
                    options.setBinary(chromeBinary);
                }
                return new ChromeDriver(options);
            }
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless)
                    options.addArguments("-headless");

                    options.addArguments("--disable-blink-features=AutomationControlled");
                    options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36");

                String firefoxBinary = System.getProperty("firefoxBinary");
                if (firefoxBinary != null && !firefoxBinary.isBlank()) {
                    options.setBinary(firefoxBinary);
                }
                return new FirefoxDriver(options);
            }
            case "edge": {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                return new EdgeDriver(options);
            }
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}


