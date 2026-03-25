Feature: Scoring

  Scenario: AC-06-a Score initialised to zero
    Given a new game starts
    When PLAYING state is entered
    Then score is initialised to 0

  Scenario: AC-06-b Score increases on brick destruction
    Given the game is in PLAYING state
    When a brick is destroyed
    Then score increases by exactly 10 points

  Scenario: AC-06-c Score visible in HUD
    Given the game is in PLAYING state
    When any frame renders
    Then the current score is visible in the top-left HUD
