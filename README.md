# Automation Framework (UI + API)

Unified Java/Maven automation framework for end-to-end UI and API testing using Cucumber BDD with TestNG, Page Object Model, Selenium, RestAssured, Log4j2, and reporting. Tests are executed via Maven only (no TestNG XML).

## Tech Stack
- Java 17, Maven
- Cucumber BDD + TestNG
- Selenium WebDriver (UI)
- RestAssured (API)
- Log4j2 (logging)
- Cucumber JSON/HTML + aggregated HTML report

## Project Structure
```
automation-framework/
  pom.xml
  README.md
  src/test/resources/
    config.properties           # baseUrl, apiBaseUrl, browser, timeout, headless
    log4j2.xml                  # logging config
    cucumber.properties         # cucumber defaults
    features/
      ui/
        sample_ui.feature
        google_search.feature   # tagged @Smoke @Sanity
      api/
        sample_api.feature
        posts.feature           # tagged @Smoke @Sanity
  src/test/java/
    com/example/framework/
      config/ConfigLoader.java
      driver/DriverManager.java
      pages/BasePage.java
      pages/HomePage.java
      pages/GoogleHomePage.java
      hooks/Hooks.java
      util/ScreenshotUtil.java
    com/example/tests/
      runners/RunCucumberUITests.java
      runners/RunCucumberAPITests.java
      steps/ui/UISteps.java
      steps/ui/GoogleSteps.java
      steps/api/APISteps.java
```

## Prerequisites
- JDK 17+
- Maven 3.8+
- Chrome/Firefox/Edge installed (choose with -Dbrowser)

## Configuration
Edit `src/test/resources/config.properties` or override via system properties:
- baseUrl (default: https://example.org)
- apiBaseUrl (default: https://jsonplaceholder.typicode.com)
- browser (chrome|firefox|edge)
- headless (true|false)
- timeoutSeconds (int)

Example overrides:
```bash
mvn -f ./pom.xml test -Dbrowser=chrome -Dheadless=true -DbaseUrl=https://example.org -DapiBaseUrl=https://jsonplaceholder.typicode.com
```

### UI browser parameterization
- Choose browser at runtime with `-Dbrowser=chrome|firefox|edge` (falls back to `config.properties`).
- Headless mode: `-Dheadless=true`.
- Custom browser binary (optional):
  - Chrome: `-DchromeBinary="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"`
  - Firefox: `-DfirefoxBinary="/Applications/Firefox.app/Contents/MacOS/firefox"`

Examples:
```bash
# Chrome headless
mvn test -Psequential -Dbrowser=chrome -Dheadless=true

# Firefox parallel (4 threads)
mvn test -Pparallel -DthreadCount=4 -Dbrowser=firefox -Dheadless=true

# Edge UI tests
mvn test -Psequential -Dbrowser=edge -Dcucumber.filter.tags="@ui"

# Point to a custom Chrome binary
mvn test -Psequential -Dbrowser=chrome -DchromeBinary="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
```

## Running Tests
Always specify the execution mode (`-Psequential` or `-Pparallel`). If omitted, the framework defaults to sequential and echoes the active mode at build start.

- Sequential (default):
```bash
mvn -f ./pom.xml test -Psequential
```
- Parallel:
```bash
mvn -f ./pom.xml test -Pparallel -DthreadCount=4
```
- UI only / API only:
```bash
mvn -f ./pom.xml -Dtest=RunCucumberUITests test
mvn -f ./pom.xml -Dtest=RunCucumberAPITests test
```
- Filter by tags (e.g., @Smoke):
```bash
mvn -f ./pom.xml test -Dcucumber.filter.tags="@Smoke"
```

From inside the project directory (`automation-framework`), you can omit `-f ./pom.xml`:
```bash
# Sequential (default)
mvn test -Psequential
# Parallel example (4 threads)
mvn test -Pparallel -DthreadCount=4
# UI only / API only
mvn -Dtest=RunCucumberUITests test
mvn -Dtest=RunCucumberAPITests test
# Filter by tag
mvn test -Dcucumber.filter.tags="@Smoke"
```

### Execution scenarios (tag-based filtering)
- 3 API + 3 UI in parallel (together):
```bash
mvn test -Pparallel -DthreadCount=4 \
  -Dtest=RunCucumberUITests,RunCucumberAPITests \
  -Dcucumber.filter.tags="(@ui and @google and @Sanity) or (@api and @posts)"
```

- Only UI in parallel:
```bash
mvn test -Pparallel -DthreadCount=4 -Dtest=RunCucumberUITests -Dcucumber.filter.tags="@ui"
```

- Exactly 2 UI tests sequential (non-@Smoke Google scenarios):
```bash
mvn test -Psequential -Dtest=RunCucumberUITests -Dcucumber.filter.tags="@ui and @google and @Sanity and not @Smoke"
```

## Tags
- UI scenarios: `@ui`, `@google`, plus `@Smoke`/`@Sanity`.
- API scenarios: `@api`, `@posts`, plus `@Smoke`/`@Sanity`.

## Reporting
- Cucumber: `target/cucumber-reports/cucumber.json` and `cucumber.html`
- Aggregated HTML: `target/cucumber-reports/cucumber-html-reports/` (generated in `verify` phase)

Generate reports only (after tests ran):
```bash
mvn -f ./pom.xml verify
```

## Screenshots on Failure
- On scenario failure, a PNG is attached to the Cucumber report and saved to `target/screenshots/<scenario>_<timestamp>.png`.
- Implemented via `Hooks.attachScreenshotOnFailure` and `ScreenshotUtil`.

## Design Notes
- Page Object Model:
  - `BasePage` provides waits/utilities; concrete pages extend it.
  - `DriverManager` uses ThreadLocal to isolate browsers in parallel.
- BDD Steps:
  - UI steps interact with page objects only.
  - API steps use RestAssured; base URI from `apiBaseUrl`.
- No TestNG XML: Maven Surefire discovers TestNG+Cucumber runners.

## Extending
- Add a UI page: create `com.example.framework.pages.NewPage` extending `BasePage`.
- Add UI steps: implement in `com.example.tests.steps.ui`.
- Add API steps: implement in `com.example.tests.steps.api`.
- Add features: create `.feature` files under `src/test/resources/features/ui|api` with `@Smoke`/`@Sanity` tags.

## Useful URLs
- JSONPlaceholder Posts API used in examples: `https://jsonplaceholder.typicode.com/posts`

## Troubleshooting
- Browser not launching: ensure browser installed; consider `-Dheadless=true` in CI.
- Parallel flakiness: increase `timeoutSeconds`, ensure stable locators.
- Reports missing: ensure tests ran; then run `mvn verify` to regenerate aggregated report.

## License
This project is provided as-is for demonstration/testing purposes.
