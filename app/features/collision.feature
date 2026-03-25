Feature: Collision Accuracy

  Scenario: AC-05-a Ball does not tunnel through bricks
    Given the ball approaches a brick from any direction
    When the circle intersects the AABB
    Then the correct velocity component is reversed
    And the ball does not tunnel through

  Scenario: AC-05-b Ball vs paddle adjusts X velocity by offset
    Given the ball approaches the paddle from above
    When the circle intersects the paddle AABB
    Then Y velocity is negated
    And X velocity is adjusted by offset from paddle centre

  Scenario: AC-05-c Speed magnitude preserved after reflection
    Given the ball is in motion
    When it contacts any surface
    Then speed magnitude is preserved after reflection
