Feature: Fire one courier

  Background:
    Given I am logged in as the platform admin and I am on couriers page

  Scenario: Fire one courier
    Given I see 4 couriers
    Given I see the courier 3 named "Lionel"
    When I press the courier 3 fire button
    Then A successfully fired this courier message should appear
    Then I see 3 couriers
