Feature: Game Over

  Scenario: AC-07-a Ball exits bottom triggers GAME_OVER
    Given the ball exits the bottom of the screen
    When detected during the update step
    Then game transitions to GAME_OVER immediately

  Scenario: AC-07-b Game Over screen content
    Given the game is in GAME_OVER state
    When the screen renders
    Then "Game Over" is displayed
    And the final score is displayed

  Scenario: AC-07-c Tap restarts from GAME_OVER
    Given the game is in GAME_OVER state
    When the player taps the screen
    Then ball paddle bricks and score are reset
    And the game returns to START state
