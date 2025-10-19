Feature: Sample UI validation
  As a user I want to validate the example site title

  Scenario: Verify example.org home page title
    Given I open the home page
    Then the page title should contain "Example"

