Feature: Win Condition

  Scenario: AC-08-a Last brick triggers WIN
    Given the last brick is destroyed
    When active brick count reaches 0
    Then game transitions to WIN immediately

  Scenario: AC-08-b Win screen content
    Given the game is in WIN state
    When the screen renders
    Then "You Win!" is displayed
    And the final score is displayed

  Scenario: AC-08-c Tap restarts from WIN
    Given the game is in WIN state
    When the player taps the screen
    Then ball paddle bricks and score are reset
    And the game returns to START state
