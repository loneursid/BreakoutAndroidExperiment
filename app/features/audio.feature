Feature: Audio

  Scenario: AC-10-b Paddle hit sound plays once
    Given the game is in PLAYING state and audio is not muted
    When the ball collides with the paddle
    Then the paddle hit sound plays exactly once per collision

  Scenario: AC-10-c Wall hit sound plays once
    Given the game is in PLAYING state and audio is not muted
    When the ball reflects off a wall
    Then the wall hit sound plays exactly once per reflection

  Scenario: AC-10-d Brick hit sound plays once
    Given the game is in PLAYING state and audio is not muted
    When the ball destroys a brick
    Then the brick hit sound plays exactly once

  Scenario: AC-10-e Ball lost sound plays
    Given the game is in PLAYING state and audio is not muted
    When the ball exits the bottom of the screen
    Then the ball lost sound plays once before GAME_OVER transition

  Scenario: AC-10-f Win sound plays
    Given the last brick is destroyed and audio is not muted
    When game transitions to WIN
    Then the win sound plays once

  Scenario: AC-10-g Tap M mutes audio
    Given audio is unmuted
    When the player taps the M button
    Then mute flag is true
    And HUD shows muted icon

  Scenario: AC-10-h Tap M unmutes audio
    Given audio is muted
    When the player taps the M button
    Then mute flag is false
    And HUD shows unmuted icon

  Scenario: AC-10-i Mute persists across state transitions
    Given audio is muted during PLAYING state
    When game transitions to any other state and back
    Then mute flag remains true

  Scenario: AC-10-j No sound plays when muted
    Given mute flag is true
    When any triggering event occurs
    Then no sound plays

  Scenario: AC-10-k No double triggering
    Given the game is in PLAYING state and audio is not muted
    When a single collision event is detected
    Then exactly one sound plays
