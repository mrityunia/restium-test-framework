package steps.ui;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.config.ConfigLoader;
import org.com.pages.HomePage;
import org.testng.Assert;

public class UISteps {
    private static final Logger logger = LogManager.getLogger(UISteps.class);
    private final HomePage homePage = new HomePage(ConfigLoader.getInt("timeoutSeconds"));

    @Given("I open the home page")
    public void i_open_the_home_page() {
        logger.info("Opening home page");
        homePage.open();
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expected) {
        String title = homePage.getTitle();
        logger.info("Page title: {}", title);
        Assert.assertTrue(title.contains(expected), "Expected title to contain '" + expected + "' but was '" + title + "'");
    }
}


