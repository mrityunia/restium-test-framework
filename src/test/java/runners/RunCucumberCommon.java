package runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {"src/test/resources/features/api",
                "src/test/resources/features/ui"
        },
        glue = {
                "steps.api",
                "steps.ui",
                "hooks"
        },
        plugin = {
                "pretty", // Outputs readable format to the console
                "html:target/cucumber-reports/cucumber-testReport.html", // Generates a basic HTML report
                "json:target/cucumber-reports/CucumberTestReport.json", // Generates a JSON report
                "rerun:target/cucumber-reports/rerun.txt" // Logs failed scenarios for re-execution
        },
        monochrome = true
)
public class RunCucumberCommon extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
