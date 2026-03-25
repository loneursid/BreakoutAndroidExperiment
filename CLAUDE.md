# Breakout Clone — Android Spec

## Overview
A Breakout arcade game clone built natively for Android using Kotlin and the Android SDK.
This is a standalone project in its own repository — no code is shared with the Windows/C++ implementation.
Single level, core mechanics, touch input, portrait orientation, phone and tablet support.

---

## Tech Stack
- **Language**: Kotlin (latest stable)
- **Platform**: Android (API 29 minimum / API 34 target)
- **IDE**: Android Studio (latest stable)
- **Build**: Gradle (Kotlin DSL)
- **Rendering**: SurfaceView + Canvas (2D, no OpenGL)
- **Audio**: Procedural PCM via AudioTrack, playback via SoundPool (no audio files)
- **BDD framework**: Cucumber-Android (Gherkin .feature files)
- **TDD framework**: JUnit5 + MockK
- **UI tests**: Espresso (for state and HUD verification)
- **Orientation**: Portrait only (locked)

---

## Project Structure
```
breakout-android/
├── build.gradle.kts
├── settings.gradle.kts
├── CLAUDE.md
├── app/
│   ├── build.gradle.kts
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── kotlin/com/breakout/
│   │   │   │   ├── MainActivity.kt           # Entry point, lifecycle owner
│   │   │   │   ├── GameView.kt               # SurfaceView subclass, game loop
│   │   │   │   ├── Game.kt                   # State machine, orchestration
│   │   │   │   ├── Ball.kt                   # Position, velocity, reflection
│   │   │   │   ├── Paddle.kt                 # Position, touch-driven movement
│   │   │   │   ├── Brick.kt                  # Position, color, active state
│   │   │   │   ├── CollisionDetector.kt      # AABB and circle-vs-rect logic
│   │   │   │   ├── ScoreManager.kt           # Score tracking and formatting
│   │   │   │   ├── Renderer.kt               # All Canvas draw calls
│   │   │   │   ├── AudioManager.kt           # Procedural sound, SoundPool, mute
│   │   │   │   └── Constants.kt              # All magic numbers in one place
│   │   │   └── res/
│   │   │       └── layout/
│   │   │           └── activity_main.xml
│   │   ├── test/                             # JUnit5 unit tests — owned by Claude Code
│   │   │   └── kotlin/com/breakout/
│   │   │       ├── BallTest.kt
│   │   │       ├── PaddleTest.kt
│   │   │       ├── BrickTest.kt
│   │   │       ├── CollisionDetectorTest.kt
│   │   │       ├── ScoreManagerTest.kt
│   │   │       ├── GameStateTest.kt
│   │   │       └── AudioManagerTest.kt
│   │   └── androidTest/                      # BDD + Espresso — features IMMUTABLE
│   │       └── kotlin/com/breakout/
│   │           ├── CucumberTestRunner.kt
│   │           └── stepdefs/
│   │               ├── StartupSteps.kt
│   │               ├── PaddleSteps.kt
│   │               ├── BallSteps.kt
│   │               ├── BrickSteps.kt
│   │               ├── CollisionSteps.kt
│   │               ├── ScoringSteps.kt
│   │               ├── GameStateSteps.kt
│   │               └── AudioSteps.kt
│   └── features/                             # *** IMMUTABLE — owned by product owner ***
│       ├── startup.feature
│       ├── paddle.feature
│       ├── ball.feature
│       ├── bricks.feature
│       ├── collision.feature
│       ├── scoring.feature
│       ├── game_over.feature
│       ├── win_condition.feature
│       ├── audio.feature
│       └── code_quality.feature
```

---

## Game Specification

### Screen and Layout
- Orientation: Portrait only (locked in AndroidManifest)
- Rendering surface: Full screen SurfaceView, edge to edge
- Background: Black
- All game dimensions expressed as fractions of screen width/height for device independence
- Target FPS: 60 (via SurfaceView game loop with delta time)

### Paddle
- Width: 22% of screen width (wider than Windows version for touch accuracy)
- Height: 1.8% of screen height
- Positioned 88% down the screen vertically
- Controlled by touch: tap and hold left half of screen = move left, tap and hold right half = move right
- Speed: 45% of screen width per second (delta-time scaled)
- Clamped to screen bounds — cannot leave the screen

### Ball
- Radius: 1.2% of screen width
- Starting position: Horizontally centred, 10% above the paddle
- Starting velocity: 28% of screen height per second (slower than Windows for mobile playability)
- Starting angle: 45 degrees up-right
- Reflects off left, right, and top edges
- Reflects off paddle on contact
- If it exits the bottom: game over

### Bricks
- Grid: 8 columns × 5 rows (narrower than Windows to suit portrait)
- Brick width: (screen width − margins) / 8 columns with 4px gaps
- Brick height: 3% of screen height
- Top margin: 12% of screen height
- Horizontal margin: 4% of screen width each side
- All bricks start with 1 hit point
- Row colors (top to bottom): Red, Orange, Yellow, Green, Blue

### Scoring
- Each brick destroyed: +10 points
- Score displayed top-left in HUD
- No high score persistence required

### Game States
1. **START** — Title and "Tap to start" prompt
2. **PLAYING** — Active gameplay
3. **GAME_OVER** — Ball lost, show "Game Over", score, "Tap to restart"
4. **WIN** — All bricks cleared, show "You Win!", score, "Tap to restart"

### Input
- START state: tap anywhere → PLAYING
- PLAYING state: hold left half → paddle moves left, hold right half → paddle moves right
- GAME_OVER / WIN state: tap anywhere → restart → START
- M button (on-screen, top-right corner, 44×44dp tap target) → toggle mute

### Collision Detection
- Ball vs walls: reverse appropriate velocity component
- Ball vs paddle: reverse Y velocity; adjust X velocity based on hit offset from paddle centre
- Ball vs brick: reverse appropriate velocity component, mark brick inactive, add score
- Use AABB for bricks and paddle, circle bounding for ball

---

## Audio Specification

### Approach
All sounds generated procedurally at runtime — no audio files in the repo.
Raw PCM waveforms generated via `AudioTrack`, loaded into `SoundPool` for low-latency playback.
`AudioManager` owns all sound generation, storage, playback, and mute state.

### Sound Design
| Event | Character | Frequency | Duration |
|---|---|---|---|
| Ball hits paddle | Mid thump | ~180 Hz sine | ~80ms |
| Ball hits wall | High bing | ~440 Hz sine | ~60ms |
| Ball destroys brick | Crisp ding | ~600 Hz sine, fast decay | ~50ms |
| Ball exits bottom | Low thud | ~90 Hz sine | ~120ms |
| Win condition | Rising sweep | ~523→1047 Hz | ~400ms |

### Mute
- On-screen M button (top-right, 44×44dp) toggles mute
- HUD shows "🔊" when unmuted, "🔇" when muted
- Mute state persists across game state transitions
- When muted all play calls are no-ops

---

## Two-Layer Test Strategy

```
┌─────────────────────────────────────────────────────────────┐
│  LAYER 1 — BDD Acceptance Tests                             │
│  Owner: Product Owner (you)                                 │
│  Framework: Cucumber-Android + Gherkin .feature files       │
│  Runner: CucumberTestRunner (androidTest)                   │
│  Scope: Behaviour observable from outside                   │
│  Location: app/features/                                    │
│  Mutability: IMMUTABLE without explicit owner approval      │
├─────────────────────────────────────────────────────────────┤
│  LAYER 2 — TDD Unit Tests                                   │
│  Owner: Claude Code                                         │
│  Framework: JUnit5 + MockK                                  │
│  Scope: Internal logic, headless, no Android runtime needed │
│  Location: app/src/test/                                    │
│  Mutability: Freely evolves as design changes               │
└─────────────────────────────────────────────────────────────┘
```

### BDD Ownership Rules for Claude Code
- **NEVER modify, rename, or delete any `.feature` file** without explicit written approval
- **NEVER modify existing step definitions** — only add new ones when new feature files are created
- **NEVER add scenarios to existing feature files**
- If a `.feature` file and the implementation conflict, fix the implementation

### TDD Rules for Claude Code
1. Write the failing test first — no implementation without a test demanding it
2. Write the minimum code to pass
3. Refactor after green
4. No Android framework calls in unit tests — use MockK to mock Android dependencies
5. Unit tests are expendable — rewrite freely as long as BDD layer stays green
6. A feature is only done when both layers pass

### Example Unit Test Pattern
```kotlin
// app/src/test/kotlin/com/breakout/BallTest.kt

// Covers AC-03-b
@Test
fun `ball reflects off left wall`() {
    val ball = Ball(screenWidth = 1080f, screenHeight = 1920f)
    ball.setPosition(ball.radius, 960f)       // touching left wall
    ball.setVelocity(-300f, -300f)

    ball.update(deltaTime = 0.016f)

    assertTrue(ball.velocity.x > 0f)          // X reversed
    assertEquals(-300f, ball.velocity.y, 1f)  // Y unchanged
}
```

---

## Acceptance Criteria

All criteria are written in Given / When / Then format.
Every criterion maps to at least one unit test and one BDD scenario.

### AC-01: Startup

**AC-01-a**
- **Given** the app is launched
- **When** MainActivity initialises
- **Then** the game displays in portrait orientation, full screen, with a black background

**AC-01-b**
- **Given** the app has launched
- **When** the START screen renders
- **Then** the game title and "Tap to start" prompt are visible

**AC-01-c**
- **Given** the START screen is active
- **When** the player taps the screen
- **Then** the game transitions to PLAYING state

---

### AC-02: Paddle Behaviour

**AC-02-a**
- **Given** the game enters PLAYING state
- **When** the first frame renders
- **Then** the paddle is centred horizontally at 88% screen height, width is 22% of screen width

**AC-02-b**
- **Given** the game is in PLAYING state and the paddle is not at the left edge
- **When** the player holds the left half of the screen
- **Then** the paddle moves left at 45% screen width per second (delta-time scaled)

**AC-02-c**
- **Given** the game is in PLAYING state and the paddle is not at the right edge
- **When** the player holds the right half of the screen
- **Then** the paddle moves right at 45% screen width per second (delta-time scaled)

**AC-02-d**
- **Given** the paddle is at the left screen edge
- **When** the player holds the left half of the screen
- **Then** the paddle position does not decrease below x = 0

**AC-02-e**
- **Given** the paddle is at the right screen edge
- **When** the player holds the right half of the screen
- **Then** the paddle position does not increase beyond x = screenWidth − paddleWidth

---

### AC-03: Ball Behaviour

**AC-03-a**
- **Given** the game transitions to PLAYING state
- **When** the first update tick runs
- **Then** the ball starts centred horizontally, 10% above the paddle, moving at 45 degrees up-right at 28% screen height per second

**AC-03-b**
- **Given** the ball is moving left and its left edge reaches x = 0
- **When** the collision is detected
- **Then** the ball's X velocity is negated and it moves rightward

**AC-03-c**
- **Given** the ball is moving right and its right edge reaches screenWidth
- **When** the collision is detected
- **Then** the ball's X velocity is negated and it moves leftward

**AC-03-d**
- **Given** the ball is moving upward and its top edge reaches y = 0
- **When** the collision is detected
- **Then** the ball's Y velocity is negated and it moves downward

**AC-03-e**
- **Given** the ball is moving downward and overlaps the paddle rect
- **When** the collision is detected
- **Then** the ball's Y velocity is negated and it moves upward

**AC-03-f**
- **Given** the ball is moving downward and its bottom edge exceeds screenHeight
- **When** this is detected
- **Then** the game transitions to GAME_OVER state

---

### AC-04: Brick Grid

**AC-04-a**
- **Given** the game enters PLAYING state
- **When** the brick grid initialises
- **Then** exactly 40 bricks (8 columns × 5 rows) are active and visible

**AC-04-b**
- **Given** the brick grid renders
- **When** rows are drawn top to bottom
- **Then** row colors are Red, Orange, Yellow, Green, Blue respectively

**AC-04-c**
- **Given** an active brick exists and the ball's circle overlaps its AABB
- **When** the collision is processed
- **Then** the brick is marked inactive, removed from display, and 10 points added to score

---

### AC-05: Collision Accuracy

**AC-05-a**
- **Given** the ball approaches a brick from any direction
- **When** the circle intersects the AABB
- **Then** the correct velocity component is reversed and the ball does not tunnel through

**AC-05-b**
- **Given** the ball approaches the paddle from above
- **When** the circle intersects the paddle AABB
- **Then** Y velocity is negated; X velocity adjusted by offset from paddle centre

**AC-05-c**
- **Given** the ball is in motion
- **When** it contacts any surface
- **Then** speed magnitude is preserved after reflection

---

### AC-06: Scoring

**AC-06-a**
- **Given** a new game starts
- **When** PLAYING state is entered
- **Then** score is initialised to 0

**AC-06-b**
- **Given** the game is in PLAYING state
- **When** a brick is destroyed
- **Then** score increases by exactly 10 points

**AC-06-c**
- **Given** the game is in PLAYING state
- **When** any frame renders
- **Then** the current score is visible in the top-left HUD

---

### AC-07: Game Over

**AC-07-a**
- **Given** the ball exits the bottom of the screen
- **When** detected during the update step
- **Then** game transitions to GAME_OVER immediately

**AC-07-b**
- **Given** the game is in GAME_OVER state
- **When** the screen renders
- **Then** "Game Over" and the final score are displayed

**AC-07-c**
- **Given** the game is in GAME_OVER state
- **When** the player taps the screen
- **Then** ball, paddle, bricks, and score fully reset and game returns to START

---

### AC-08: Win Condition

**AC-08-a**
- **Given** the last brick is destroyed
- **When** active brick count reaches 0
- **Then** game transitions to WIN immediately

**AC-08-b**
- **Given** the game is in WIN state
- **When** the screen renders
- **Then** "You Win!" and the final score are displayed

**AC-08-c**
- **Given** the game is in WIN state
- **When** the player taps the screen
- **Then** ball, paddle, bricks, and score fully reset and game returns to START

---

### AC-09: Code Quality

**AC-09-a**
- **Given** the codebase is reviewed
- **When** checking for global state
- **Then** no mutable global variables exist — all state owned by Game class

**AC-09-b**
- **Given** the codebase is reviewed
- **When** checking for magic numbers
- **Then** all numeric constants are defined in Constants.kt

**AC-09-c**
- **Given** the project is built
- **When** compiled with default Kotlin warnings enabled
- **Then** zero warnings are emitted

**AC-09-d**
- **Given** the project Gradle files are present
- **When** `./gradlew assembleDebug` is run
- **Then** a runnable APK is produced that installs on API 29+ devices

---

### AC-10: Audio

**AC-10-a**
- **Given** the game launches
- **When** AudioManager initialises
- **Then** SoundPool is ready and all five sounds are loaded with no errors

**AC-10-b**
- **Given** the game is in PLAYING state and audio is not muted
- **When** the ball collides with the paddle
- **Then** the paddle hit sound plays exactly once per collision

**AC-10-c**
- **Given** the game is in PLAYING state and audio is not muted
- **When** the ball reflects off a wall
- **Then** the wall hit sound plays exactly once per reflection

**AC-10-d**
- **Given** the game is in PLAYING state and audio is not muted
- **When** the ball destroys a brick
- **Then** the brick hit sound plays exactly once

**AC-10-e**
- **Given** the game is in PLAYING state and audio is not muted
- **When** the ball exits the bottom of the screen
- **Then** the ball lost sound plays once before GAME_OVER transition

**AC-10-f**
- **Given** the last brick is destroyed and audio is not muted
- **When** game transitions to WIN
- **Then** the win sound plays once

**AC-10-g**
- **Given** audio is unmuted
- **When** the player taps the M button
- **Then** mute flag is true and HUD shows 🔇

**AC-10-h**
- **Given** audio is muted
- **When** the player taps the M button
- **Then** mute flag is false and HUD shows 🔊

**AC-10-i**
- **Given** audio is muted during PLAYING state
- **When** game transitions to any other state and back
- **Then** mute flag remains true

**AC-10-j**
- **Given** mute flag is true
- **When** any triggering event occurs
- **Then** no sound plays

**AC-10-k**
- **Given** the game is in PLAYING state and audio is not muted
- **When** a single collision event is detected
- **Then** exactly one sound plays — no stacking or double-triggering

---

### AC-11: Device Adaptability

**AC-11-a**
- **Given** the game runs on a phone (e.g. 1080×1920)
- **When** any frame renders
- **Then** all game elements are correctly sized and positioned as screen fractions

**AC-11-b**
- **Given** the game runs on a tablet (e.g. 1600×2560)
- **When** any frame renders
- **Then** all game elements scale proportionally — no clipping, no misalignment

**AC-11-c**
- **Given** the game is running
- **When** the device screen density varies (hdpi, xhdpi, xxhdpi, xxxhdpi)
- **Then** tap targets (paddle control zones, M button) remain correctly sized and responsive

---

## SOLID Principles

### Single Responsibility
- `Ball` — position, velocity, wall reflection logic only
- `Paddle` — position and touch-driven movement only
- `Brick` — position, color, active state only
- `Game` — state transitions and orchestration only
- `CollisionDetector` — all AABB and circle-vs-rect logic
- `ScoreManager` — score tracking and formatting
- `Renderer` — all Canvas draw calls
- `AudioManager` — PCM generation, SoundPool management, mute state
- `GameView` — SurfaceView game loop, delta time, lifecycle bridge only

### Open/Closed
- State transitions driven by enum, not if/else chains
- New collidable types do not require modifying existing collision code

### Liskov Substitution
- Any interface implementations must honour the full contract — no no-op overrides

### Interface Segregation
- Prefer narrow interfaces: `Updatable`, `Drawable`, `Collidable`
- No fat `GameObject` interface

### Dependency Inversion
- `Game` depends on abstractions not concretions where practical
- `CollisionDetector` accepts interface types enabling MockK substitution in tests

---

## Implementation Notes for Claude Code

### Setup (first session)
1. Create new Android project in Android Studio: Empty Activity, Kotlin, API 29 minimum
2. Add dependencies to `app/build.gradle.kts`:
   - `junit5` for unit tests
   - `mockk` for mocking Android dependencies in unit tests
   - `cucumber-android` for BDD
   - `espresso-core` for UI step definitions
3. Lock orientation in `AndroidManifest.xml`: `android:screenOrientation="portrait"`
4. Enable edge-to-edge display

### Game Loop Pattern
```kotlin
// GameView.kt — SurfaceView game loop
class GameView(context: Context) : SurfaceView(context), Runnable {
    private var thread: Thread? = null
    private var isRunning = false

    override fun run() {
        var lastTime = System.nanoTime()
        while (isRunning) {
            val now = System.nanoTime()
            val deltaTime = (now - lastTime) / 1_000_000_000f
            lastTime = now
            game.update(deltaTime)
            drawFrame()
            // cap to ~60fps
        }
    }
}
```

### Dimension Pattern
```kotlin
// Constants.kt — all dimensions as fractions
object Constants {
    const val PADDLE_WIDTH_FRACTION = 0.22f
    const val PADDLE_HEIGHT_FRACTION = 0.018f
    const val PADDLE_Y_FRACTION = 0.88f
    const val BALL_RADIUS_FRACTION = 0.012f
    const val BALL_SPEED_FRACTION = 0.28f   // of screenHeight per second
    // etc.
}

// Usage in game classes
val paddleWidth = screenWidth * Constants.PADDLE_WIDTH_FRACTION
```

### Audio Pattern
```kotlin
// AudioManager.kt
class AudioManager(private val context: Context) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .build()

    private val sounds = mutableMapOf<SoundEvent, Int>()
    var isMuted = false

    init {
        SoundEvent.values().forEach { event ->
            val pcm = generatePcm(event)
            sounds[event] = loadFromPcm(pcm)
        }
    }

    fun play(event: SoundEvent) {
        if (!isMuted) soundPool.play(sounds[event]!!, 1f, 1f, 0, 0, 1f)
    }

    fun toggleMute() { isMuted = !isMuted }

    private fun generatePcm(event: SoundEvent): ShortArray { /* synthesise */ }
}
```

### TDD Workflow per Feature
1. Read the `.feature` file for the target behaviour
2. Write a failing JUnit5 unit test
3. Write minimum Kotlin to pass
4. Refactor
5. Write Cucumber-Android step definitions wiring the feature file
6. Run BDD suite on emulator — all scenarios green before moving on
7. Run unit test suite — all tests green
8. Only then move to the next feature

### Build Commands
```bash
# Unit tests (fast, headless)
./gradlew test

# BDD + instrumented tests (requires emulator or device)
./gradlew connectedAndroidTest

# Build debug APK
./gradlew assembleDebug
```

---

## Out of Scope (v1)
- Multiple levels
- Lives system (ball lost = immediate game over)
- Power-ups
- Background music
- High score persistence
- Tilt/accelerometer control
- Landscape orientation
- Google Play Store submission
