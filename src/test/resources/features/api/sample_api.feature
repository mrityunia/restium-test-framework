Feature: Sample API validation
  Validate a simple GET API response

  Scenario: Verify GET /posts/1 returns 200 and has id 1
    Given I set the API base url
    When I GET "/posts/1"
    Then the response status should be 200
    And the JSON path "id" should equal 1

