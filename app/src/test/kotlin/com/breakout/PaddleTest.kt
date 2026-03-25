package com.breakout

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PaddleTest {

    private lateinit var paddle: Paddle
    private val screenWidth = 1080f
    private val screenHeight = 1920f

    @BeforeEach
    fun setUp() {
        paddle = Paddle(screenWidth, screenHeight)
    }

    // AC-02-a
    @Test
    fun `paddle is centred horizontally after reset`() {
        val expectedX = (screenWidth - paddle.width) / 2f
        assertEquals(expectedX, paddle.x, 0.1f)
    }

    @Test
    fun `paddle is at 88 percent screen height`() {
        val expectedY = screenHeight * Constants.PADDLE_Y_FRACTION
        assertEquals(expectedY, paddle.y, 0.1f)
    }

    @Test
    fun `paddle width is 22 percent of screen width`() {
        assertEquals(screenWidth * Constants.PADDLE_WIDTH_FRACTION, paddle.width, 0.1f)
    }

    // AC-02-b — use short deltaTime to avoid clamping
    @Test
    fun `paddle moves left at correct speed`() {
        paddle.setX(600f)  // far enough right that full delta fits
        val startX = paddle.x
        val dt = 0.1f
        paddle.moveLeft(dt)
        val expectedDelta = screenWidth * Constants.PADDLE_SPEED_FRACTION * dt
        assertEquals(startX - expectedDelta, paddle.x, 1f)
    }

    // AC-02-c
    @Test
    fun `paddle moves right at correct speed`() {
        paddle.setX(100f)  // far enough left that full delta fits
        val startX = paddle.x
        val dt = 0.1f
        paddle.moveRight(dt)
        val expectedDelta = screenWidth * Constants.PADDLE_SPEED_FRACTION * dt
        assertEquals(startX + expectedDelta, paddle.x, 1f)
    }

    // AC-02-d
    @Test
    fun `paddle cannot move past left edge`() {
        paddle.setX(0f)
        paddle.moveLeft(1.0f)
        assertEquals(0f, paddle.x, 0.01f)
    }

    // AC-02-e
    @Test
    fun `paddle cannot move past right edge`() {
        paddle.setX(screenWidth - paddle.width)
        paddle.moveRight(1.0f)
        assertEquals(screenWidth - paddle.width, paddle.x, 0.01f)
    }
}
