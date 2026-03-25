Feature: Ball Behaviour

  Scenario: AC-03-a Ball initial state
    Given the game transitions to PLAYING state
    When the first update tick runs
    Then the ball is centred horizontally
    And the ball is 10 percent above the paddle
    And the ball is moving at 45 degrees up-right
    And the ball speed is 28 percent of screen height per second

  Scenario: AC-03-b Ball reflects off left wall
    Given the ball is moving left and its left edge reaches x equals 0
    When the collision is detected
    Then the ball X velocity is negated
    And the ball moves rightward

  Scenario: AC-03-c Ball reflects off right wall
    Given the ball is moving right and its right edge reaches screen width
    When the collision is detected
    Then the ball X velocity is negated
    And the ball moves leftward

  Scenario: AC-03-d Ball reflects off top wall
    Given the ball is moving upward and its top edge reaches y equals 0
    When the collision is detected
    Then the ball Y velocity is negated
    And the ball moves downward

  Scenario: AC-03-e Ball reflects off paddle
    Given the ball is moving downward and overlaps the paddle rect
    When the collision is detected
    Then the ball Y velocity is negated
    And the ball moves upward

  Scenario: AC-03-f Ball exits bottom triggers game over
    Given the ball is moving downward and its bottom edge exceeds screen height
    When this is detected
    Then the game transitions to GAME_OVER state
