package org.com.pages;


import org.com.config.ConfigLoader;
import org.com.driver.DriverManager;

public class HomePage extends BasePage {
    public HomePage(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    public void open() {
        DriverManager.getDriver().get(ConfigLoader.get("baseUrl"));
    }
}


