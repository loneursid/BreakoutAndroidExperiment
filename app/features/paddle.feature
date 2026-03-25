Feature: Paddle Behaviour

  Scenario: AC-02-a Paddle initial position
    Given the game enters PLAYING state
    When the first frame renders
    Then the paddle is centred horizontally
    And the paddle is at 88 percent screen height
    And the paddle width is 22 percent of screen width

  Scenario: AC-02-b Paddle moves left
    Given the game is in PLAYING state
    And the paddle is not at the left edge
    When the player holds the left half of the screen for 1 second
    Then the paddle has moved left by 45 percent of screen width

  Scenario: AC-02-c Paddle moves right
    Given the game is in PLAYING state
    And the paddle is not at the right edge
    When the player holds the right half of the screen for 1 second
    Then the paddle has moved right by 45 percent of screen width

  Scenario: AC-02-d Paddle clamped at left edge
    Given the paddle is at the left screen edge
    When the player holds the left half of the screen
    Then the paddle position does not decrease below zero

  Scenario: AC-02-e Paddle clamped at right edge
    Given the paddle is at the right screen edge
    When the player holds the right half of the screen
    Then the paddle position does not exceed screen width minus paddle width
