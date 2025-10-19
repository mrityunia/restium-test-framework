@ui @google
Feature: Google Search
  As a user I want to search on Google and verify results

  @Smoke @Sanity
  Scenario: Search for Selenium WebDriver
    Given I open Google home page
    When I search for "Selenium WebDriver"
    Then the results page title should contain "Selenium WebDriver"

  @Sanity
  Scenario: Search for Cucumber BDD
    Given I open Google home page
    When I search for "Cucumber BDD"
    Then the results page title should contain "Cucumber BDD"

  @Sanity
  Scenario: Search for RestAssured
    Given I open Google home page
    When I search for "RestAssured"
    Then the results page title should contain "RestAssured"

