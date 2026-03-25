Feature: Brick Grid

  Scenario: AC-04-a Brick count
    Given the game enters PLAYING state
    When the brick grid initialises
    Then exactly 40 bricks are active and visible

  Scenario: AC-04-b Brick row colors
    Given the brick grid renders
    When rows are drawn top to bottom
    Then row 1 is red
    And row 2 is orange
    And row 3 is yellow
    And row 4 is green
    And row 5 is blue

  Scenario: AC-04-c Ball destroys brick and scores
    Given an active brick exists and the ball overlaps its bounds
    When the collision is processed
    Then the brick is marked inactive
    And 10 points are added to the score
