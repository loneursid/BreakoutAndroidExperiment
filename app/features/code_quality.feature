Feature: Code Quality

  Scenario: AC-09-a No mutable global variables
    Given the codebase is reviewed
    When checking for global state
    Then no mutable global variables exist
    And all state is owned by the Game class

  Scenario: AC-09-b No magic numbers
    Given the codebase is reviewed
    When checking for magic numbers
    Then all numeric constants are defined in Constants

  Scenario: AC-09-c Zero compiler warnings
    Given the project is built
    When compiled with default Kotlin warnings enabled
    Then zero warnings are emitted

  Scenario: AC-09-d Build produces APK
    Given the project Gradle files are present
    When assembleDebug is run
    Then a runnable APK is produced
