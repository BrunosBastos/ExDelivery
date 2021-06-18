Feature: Login as a Client

  Background:
    Given I am a Client trying to log in

  Scenario: Introduce Invalid Information to Login
    When I insert my email 'tiago@gmail.com'
    And I insert a wrong password
    And I press the login button
    Then A failed logged in message should appear

  Scenario: Introduce Valid Information to Login
    And I insert my email 'tiago@gmail.com'
    And I insert my correct password
    And I press the login button
    Then A successfully logged in message should appear
