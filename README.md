---

# 🚀 Automation Framework (UI + API)

A **unified Java/Maven automation framework** for **end-to-end UI and API testing**, built on **Cucumber BDD** with **TestNG**, the **Page Object Model (POM)** pattern, **Selenium**, **RestAssured**, and **Log4j2** logging.

Tests are executed via **Maven** only (no TestNG XML required).
The framework supports **parallel or sequential execution**, **Selenium Grid**, **headless mode**, and **Cucumber JSON/HTML reporting**.

---

## 🧠 Tech Stack

* **Java 17**
* **Maven 3.8+**
* **Cucumber BDD + TestNG**
* **Selenium WebDriver** (UI automation)
* **RestAssured** (API testing)
* **Log4j2** (logging)
* **Cucumber JSON/HTML reports + Aggregated HTML dashboard**

---

## 📂 Project Structure

```
automation-framework/
  pom.xml
  README.md
  src/test/resources/
    config.properties           # baseUrl, apiBaseUrl, browser, timeout, headless
    log4j2.xml                  # logging configuration
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

---

## ⚙️ Prerequisites

* **JDK 17+**
* **Maven 3.8+**
* **Chrome**, **Firefox**, or **Edge** installed (selectable at runtime)

---

## ⚡ Configuration

Update `src/test/resources/config.properties` or override using system properties:

| Property         | Description           | Default                                |
| ---------------- | --------------------- | -------------------------------------- |
| `baseUrl`        | UI test base URL      | `https://example.org`                  |
| `apiBaseUrl`     | API test base URL     | `https://jsonplaceholder.typicode.com` |
| `browser`        | Target browser        | `chrome`                               |
| `headless`       | Run without UI        | `true`                                 |
| `timeoutSeconds` | Explicit wait timeout | `10`                                   |

### Example overrides:

```bash
mvn clean test \
  -Dbrowser=chrome \
  -Dheadless=true \
  -DbaseUrl=https://example.org \
  -DapiBaseUrl=https://jsonplaceholder.typicode.com
```

---

## 🧭 Browser Configuration

* Choose browser:
  `-Dbrowser=chrome|firefox|edge`

* Enable headless mode:
  `-Dheadless=true`

* Use custom browser binary (optional):

```bash
# Chrome example
-DchromeBinary="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"

# Firefox example
-DfirefoxBinary="/Applications/Firefox.app/Contents/MacOS/firefox"
```

### Examples:

```bash
# Chrome headless
mvn test -Psequential -Dbrowser=chrome -Dheadless=true

# Firefox parallel (4 threads)
mvn test -Pparallel -DthreadCount=4 -Dbrowser=firefox -Dheadless=true

# Edge UI tests
mvn test -Psequential -Dbrowser=edge -Dcucumber.filter.tags="@ui"

# Custom Chrome binary
mvn test -Psequential -Dbrowser=chrome -DchromeBinary="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
```

---

## 🧪 Running Tests

The framework supports two execution profiles:

* **Sequential** – default mode
* **Parallel** – multi-threaded execution

Always specify the execution mode using Maven profiles:

```bash
# Sequential (default)
mvn test -Psequential

# Parallel (4 threads)
mvn test -Pparallel -DthreadCount=4
```

### Targeted Runs

* **UI only**

```bash
mvn -Dtest=RunCucumberUITests test
```

* **API only**

```bash
mvn -Dtest=RunCucumberAPITests test
```

* **Filter by tags**

```bash
mvn test -Dcucumber.filter.tags="@Smoke"
```

---

### 🧩 Tag-Based Execution Examples

* Run both UI and API together (parallel):

```bash
mvn test -Pparallel -DthreadCount=4 \
  -Dcucumber.filter.tags="(@ui and @google and @Sanity) or (@api and @posts)"
```

* UI only in parallel:

```bash
mvn test -Pparallel -DthreadCount=4 \
  -Dtest=RunCucumberUITests -Dcucumber.filter.tags="@ui"
```

* Sequential run excluding `@Smoke`:

```bash
mvn test -Psequential \
  -Dtest=RunCucumberUITests \
  -Dcucumber.filter.tags="@ui and @google and @Sanity and not @Smoke"
```

---

## 🏷️ Tags

| Tag       | Scope        | Purpose                  |
| --------- | ------------ | ------------------------ |
| `@ui`     | UI features  | Marks UI feature files   |
| `@google` | UI           | Specific Google tests    |
| `@api`    | API features | Marks API feature files  |
| `@posts`  | API          | JSONPlaceholder examples |
| `@Smoke`  | General      | Lightweight sanity runs  |
| `@Sanity` | General      | Stable regression set    |

---

## 📊 Reporting

* **Cucumber JSON:** `target/cucumber-reports/cucumber.json`
* **HTML Report:** `target/cucumber-reports/cucumber.html`
* **Aggregated Dashboard:** `target/cucumber-reports/cucumber-html-reports/`

Generate reports after tests:

```bash
mvn verify
```

---

## 📸 Screenshots on Failure

* On failure, screenshots are automatically:

    * Saved under `target/screenshots/<scenario>_<timestamp>.png`
    * Embedded into the Cucumber HTML report
* Implemented via:

    * `Hooks.attachScreenshotOnFailure`
    * `ScreenshotUtil`

---

## 🧩 Design Overview

* **Page Object Model (POM):**

    * `BasePage` provides core wait & interaction utilities.
    * Individual pages extend `BasePage`.

* **Driver Management:**

    * `DriverManager` uses `ThreadLocal<WebDriver>` for thread safety in parallel runs.

* **Step Definitions:**

    * `steps/ui` interacts only with Page Objects.
    * `steps/api` uses RestAssured with `apiBaseUrl` as base URI.

* **No TestNG XML:**

    * Test discovery handled by Maven Surefire + TestNG + Cucumber.

---

## 🧱 Extending the Framework

| Task             | How to Extend                                                                  |
| ---------------- | ------------------------------------------------------------------------------ |
| Add a new page   | Create `NewPage.java` under `com.example.framework.pages` extending `BasePage` |
| Add UI steps     | Implement in `com.example.tests.steps.ui`                                      |
| Add API steps    | Implement in `com.example.tests.steps.api`                                     |
| Add new features | Create `.feature` files under `src/test/resources/features/ui` or `api`        |

---

## 🌍 Example API Used

* **JSONPlaceholder API (Demo Source):**
  [https://jsonplaceholder.typicode.com/posts](https://jsonplaceholder.typicode.com/posts)

---

## 🧰 Troubleshooting

| Issue                   | Possible Fix                                      |
| ----------------------- | ------------------------------------------------- |
| Browser not launching   | Ensure browser installed or use `-Dheadless=true` |
| Parallel instability    | Increase `timeoutSeconds`, stabilize locators     |
| Missing reports         | Ensure tests ran, then execute `mvn verify`       |
| Grid connection failure | Verify Docker network and Selenium Grid status    |

---

## 🐳 Docker & Jenkins Integration

### 🧩 Jenkins Service (Dockerized)

**Start Jenkins:**

```bash
docker compose -f docker-compose-jenkin.yml up -d --build
```

**Stop Jenkins:**

```bash
docker compose -f docker-compose-jenkin.yml down -v
```

This setup automatically provisions:

* Jenkins master node
* Maven, Java, and Git preinstalled
* Shared Docker socket for running containerized builds

---

### 🌐 Selenium Grid (Dockerized)

**Start Grid:**

```bash
docker compose -f docker-compose.selenium-grid.yml up -d
```

**Stop Grid:**

```bash
docker compose -f docker-compose.selenium-grid.yml down -v
```

**Access Selenium Grid UI:**
👉 [http://localhost:4444/ui](http://localhost:4444/ui)

---

### 🧭 Local Test Execution via Grid

Run tests against your **local Selenium Grid**:

```bash
mvn clean test \
  -Pparallel \
  -Dmode=parallel \
  -Dthreads=4 \
  -Dselenium.grid=true \
  -Dbrowser=firefox
```

---

## ⚖️ License

This project is provided **as-is** for demonstration and educational purposes.

---

