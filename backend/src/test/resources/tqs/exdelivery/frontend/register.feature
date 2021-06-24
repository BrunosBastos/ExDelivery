Feature: Register as a Client

  Background:
    Given I am a Client trying to register

  Scenario: Introduce Valid Information to Register
    When I insert an email like 'iglesias@gmail.com'
    And I insert a name like 'Iglesias'
    And I insert a password like 'string'
    And I press the register button
    Then A successfully registered message should appear

  Scenario: Introduce An Email Already in Use to Register
    When I insert an email in use like 'tiago@gmail.com'
    And I insert a name like 'Tiago'
    And I insert a password like 'string'
    And I press the register button
    Then A failed registered message should appear
