package org.com.pages;


import org.com.config.ConfigLoader;
import org.com.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class GoogleHomePage extends BasePage {
    private static final By SEARCH_BOX = By.name("q");

    public GoogleHomePage(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    public void open() {
        // Use hl=en and gl=US to reduce consent/geo interstitials; also accept consent if present
        DriverManager.getDriver().get("https://www.google.com/?hl=en&gl=US&gws_rd=cr");
        dismissConsentIfPresent();
    }

    public void search(String query) {
        WebElement box = waitForVisible(SEARCH_BOX);
        box.clear();
        box.sendKeys(query);
        box.sendKeys(Keys.ENTER);
    }

    public void waitForResultsContains(String expected) {
        WebDriver driver = DriverManager.getDriver();
        int timeout = ConfigLoader.getInt("timeoutSeconds");
        String encoded = URLEncoder.encode(expected, StandardCharsets.UTF_8);
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.or(
                        ExpectedConditions.titleContains(expected),
                        ExpectedConditions.urlContains(encoded)
                ));
    }

    private void dismissConsentIfPresent() {
        WebDriver driver = DriverManager.getDriver();
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            // Different selectors appear depending on region/UI variant
            By agreeButton = By.cssSelector("button[aria-label='Accept all']");
            By agreeButtonAlt = By.cssSelector("button[aria-label='Agree to the use of cookies and other data for the purposes described']");
            By consentForm = By.cssSelector("form[action*='consent'] button, div[role='none'] button[aria-label*='Accept']");
            WebElement btn = null;
            try { btn = shortWait.until(ExpectedConditions.elementToBeClickable(agreeButton)); } catch (Exception ignored) {}
            if (btn == null) {
                try { btn = shortWait.until(ExpectedConditions.elementToBeClickable(agreeButtonAlt)); } catch (Exception ignored) {}
            }
            if (btn == null) {
                try { btn = shortWait.until(ExpectedConditions.elementToBeClickable(consentForm)); } catch (Exception ignored) {}
            }
            if (btn != null) {
                btn.click();
            }
        } catch (Exception ignored) {
            // no consent shown
        }
    }
}


