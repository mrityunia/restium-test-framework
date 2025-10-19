@api @posts
Feature: JSONPlaceholder Posts API
  Validate the public posts API

  @Smoke
  Scenario: GET /posts returns 100 posts
    Given I set the API base url
    When I GET "/posts"
    Then the response status should be 200
    And the JSON array size should be 100

  @Sanity
  Scenario: GET /posts/1 returns post with id 1
    Given I set the API base url
    When I GET "/posts/1"
    Then the response status should be 200
    And the JSON path "id" should equal 1

  @Sanity
  Scenario Outline: GET /posts/<id> returns expected userId
    Given I set the API base url
    When I GET "/posts/<id>"
    Then the response status should be 200
    And the JSON path "userId" should equal <userId>

    Examples:
      | id | userId |
      | 1  | 1      |
      | 11 | 2      |
      | 21 | 3      |

