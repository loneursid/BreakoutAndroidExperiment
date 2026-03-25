Feature: App Startup

  Scenario: AC-01-a Portrait orientation and black background
    Given the app is launched
    When MainActivity initialises
    Then the game is in portrait orientation
    And the background is black

  Scenario: AC-01-b Start screen content
    Given the app has launched
    When the START screen renders
    Then the game title is visible
    And the "Tap to start" prompt is visible

  Scenario: AC-01-c Tap to enter playing state
    Given the START screen is active
    When the player taps the screen
    Then the game transitions to PLAYING state
