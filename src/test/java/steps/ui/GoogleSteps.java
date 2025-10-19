package steps.ui;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.config.ConfigLoader;
import org.com.driver.DriverManager;
import org.com.pages.GoogleHomePage;
import org.testng.Assert;

public class GoogleSteps {
    private static final Logger logger = LogManager.getLogger(GoogleSteps.class);
    private final GoogleHomePage google = new GoogleHomePage(ConfigLoader.getInt("timeoutSeconds"));

    @Given("I open Google home page")
    public void i_open_google_home_page() {
        logger.info("Opening Google home page");
        google.open();
    }

    @When("I search for {string}")
    public void i_search_for(String query) {
        logger.info("Searching for: {}", query);
        google.search(query);
    }

    @Then("the results page title should contain {string}")
    public void the_results_page_title_should_contain(String expected) {
        google.waitForResultsContains(expected);
        String title = google.getTitle();
        String url = DriverManager.getDriver().getCurrentUrl();
        logger.info("Results title: {} | url: {}", title, url);
        Assert.assertTrue(title.contains(expected) || url.contains(expected.replace(" ", "+")),
                "Expected title or url to contain '" + expected + "' but was title='" + title + "' url='" + url + "'");
    }
}


