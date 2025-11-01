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
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    private DriverManager() {
    }

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

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                }
                chromeOptions.addArguments("--window-size=1920,1080");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (headless) ffOptions.addArguments("-headless");
                return new FirefoxDriver(ffOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless, String gridUrl) {
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) chromeOptions.addArguments("--headless=new");
                    return new RemoteWebDriver(new URL(gridUrl), chromeOptions);

                case "firefox":
                    FirefoxOptions ffOptions = new FirefoxOptions();
                    if (headless) ffOptions.addArguments("-headless");
                    return new RemoteWebDriver(new URL(gridUrl), ffOptions);

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) edgeOptions.addArguments("--headless=new");
                    return new RemoteWebDriver(new URL(gridUrl), edgeOptions);

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Selenium Grid URL: " + gridUrl, e);
        }
    }

    private static WebDriver createDriver() {
        String browser = System.getProperty("browser", ConfigLoader.get("browser"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(ConfigLoader.getBoolean("headless"))));
        boolean useGrid = Boolean.parseBoolean(System.getProperty("selenium.grid", "false"));
        String gridUrl = System.getProperty("selenium.grid.url", "http://localhost:4444/wd/hub");

        if (useGrid) {
            return createRemoteDriver(browser, headless, gridUrl);
        } else {
            return createLocalDriver(browser, headless);
        }
    }
}


