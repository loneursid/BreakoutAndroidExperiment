package com.breakout

object Constants {
    // Paddle
    const val PADDLE_WIDTH_FRACTION = 0.22f
    const val PADDLE_HEIGHT_FRACTION = 0.018f
    const val PADDLE_Y_FRACTION = 0.88f
    const val PADDLE_SPEED_FRACTION = 0.45f  // of screen width per second

    // Ball
    const val BALL_RADIUS_FRACTION = 0.012f
    const val BALL_SPEED_FRACTION = 0.28f    // of screen height per second
    const val BALL_START_ANGLE_DEG = 45.0    // degrees up-right

    // Bricks
    const val BRICK_COLS = 8
    const val BRICK_ROWS = 5
    const val BRICK_HEIGHT_FRACTION = 0.03f
    const val BRICK_TOP_MARGIN_FRACTION = 0.12f
    const val BRICK_HORIZONTAL_MARGIN_FRACTION = 0.04f
    const val BRICK_GAP_PX = 4f
    const val BRICK_POINTS = 10

    // Ball start offset above paddle (as fraction of screen height)
    const val BALL_START_OFFSET_FRACTION = 0.10f

    // HUD
    const val HUD_TEXT_SIZE_FRACTION = 0.04f
    const val HUD_PADDING_FRACTION = 0.02f
    const val MUTE_BUTTON_DP = 44f

    // Audio sample rate
    const val AUDIO_SAMPLE_RATE = 44100

    // Target FPS
    const val TARGET_FPS = 60L
    const val FRAME_TIME_NS = 1_000_000_000L / TARGET_FPS

    // Brick row colors (ARGB, no android.graphics.Color dependency)
    const val COLOR_RED    = 0xFFFF0000.toInt()
    const val COLOR_ORANGE = 0xFFFFA500.toInt()
    const val COLOR_YELLOW = 0xFFFFFF00.toInt()
    const val COLOR_GREEN  = 0xFF00FF00.toInt()
    const val COLOR_BLUE   = 0xFF0000FF.toInt()
}
