Feature: Check Courier Deliveries

  Background:
    Given I am logged in as a courier and I am on the deliveries page

  Scenario: Check status and see list of deliveries and then press the most recent
    When I am able to see all my deliveries
    And I change the ordering of my deliveries to 'descending'
    And I am able to see a delivery status
    Then I press my latest one and I am redirected to delivery details page