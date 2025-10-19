package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/api",
        glue = {
                "steps.api",
                "hooks"},
        plugin = {"pretty", "json:target/cucumber-reports/cucumber.json",
                            "html:target/cucumber-reports/cucumber.html"},
        monochrome = true
)
public class RunCucumberAPITests extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}


