package steps.api;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.config.ConfigLoader;
import org.testng.Assert;

public class APISteps {
    private static final Logger logger = LogManager.getLogger(APISteps.class);
    private Response response;

    @Given("I set the API base url")
    public void i_set_the_api_base_url() {
        RestAssured.baseURI = System.getProperty("apiBaseUrl", ConfigLoader.get("apiBaseUrl"));
        logger.info("API base URI set to {}", RestAssured.baseURI);
    }

    @When("I GET {string}")
    public void i_get(String path) {
        response = RestAssured.given().when().get(path);
        logger.info("GET {} -> status {}", path, response.statusCode());
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        Assert.assertEquals(response.statusCode(), expectedStatus, "Unexpected HTTP status");
    }

    @Then("the JSON path {string} should equal {int}")
    public void the_json_path_should_equal(String jsonPath, int expected) {
        int actual = response.jsonPath().getInt(jsonPath);
        Assert.assertEquals(actual, expected, "Unexpected JSON int at path " + jsonPath);
    }

    @Then("the JSON array size should be {int}")
    public void the_json_array_size_should_be(int expectedSize) {
        int size = response.jsonPath().getList("$").size();
        Assert.assertEquals(size, expectedSize, "Unexpected array size");
    }
}


