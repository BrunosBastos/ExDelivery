Feature: Check Platform Deliveries

  Background:
    Given I am logged in as the platform admin and I am on platform deliveries page

  Scenario: Order list of deliveries and then press the most recent
    When I am able to see a few deliveries
    And I change the ordering option to 'descending'
    And I press the search button
    Then I press the first one and I am redirected to delivery details page

  Scenario: Find deliveries of a courier
    When I am able to see a few deliveries
    And I search for a courier with the email 'tiago@gmail.com'
    And I press the search button
    Then I press the first one and I am redirected to delivery details page