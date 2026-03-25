package com.breakout

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.abs

class BallTest {

    private lateinit var ball: Ball
    private val screenWidth = 1080f
    private val screenHeight = 1920f

    @BeforeEach
    fun setUp() {
        ball = Ball(screenWidth, screenHeight)
        ball.reset(paddleY = screenHeight * 0.88f)
    }

    // AC-03-a
    @Test
    fun `ball starts centred horizontally`() {
        assertEquals(screenWidth / 2f, ball.position.x, 1f)
    }

    @Test
    fun `ball starts 10 percent above paddle`() {
        val paddleY = screenHeight * 0.88f
        val expectedY = paddleY - screenHeight * Constants.BALL_START_OFFSET_FRACTION
        assertEquals(expectedY, ball.position.y, 1f)
    }

    @Test
    fun `ball starts moving at 45 degrees up right`() {
        assertTrue(ball.velocity.x > 0f)
        assertTrue(ball.velocity.y < 0f)
        assertEquals(abs(ball.velocity.x), abs(ball.velocity.y), 1f)
    }

    @Test
    fun `ball speed is 28 percent of screen height`() {
        val expected = screenHeight * Constants.BALL_SPEED_FRACTION
        val actual = kotlin.math.sqrt(ball.velocity.x * ball.velocity.x + ball.velocity.y * ball.velocity.y)
        assertEquals(expected, actual, 2f)
    }

    // AC-03-b
    @Test
    fun `ball reflects off left wall`() {
        ball.setPosition(ball.radius, 960f)
        ball.setVelocity(-300f, -300f)
        ball.update(deltaTime = 0.016f)
        assertTrue(ball.velocity.x > 0f)
        assertEquals(-300f, ball.velocity.y, 1f)
    }

    // AC-03-c
    @Test
    fun `ball reflects off right wall`() {
        ball.setPosition(screenWidth - ball.radius, 960f)
        ball.setVelocity(300f, -300f)
        ball.update(deltaTime = 0.016f)
        assertTrue(ball.velocity.x < 0f)
        assertEquals(-300f, ball.velocity.y, 1f)
    }

    // AC-03-d
    @Test
    fun `ball reflects off top wall`() {
        ball.setPosition(540f, ball.radius)
        ball.setVelocity(300f, -300f)
        ball.update(deltaTime = 0.016f)
        assertTrue(ball.velocity.y > 0f)
        assertEquals(300f, ball.velocity.x, 1f)
    }

    // AC-03-f
    @Test
    fun `ball isBelow returns true when below screen`() {
        ball.setPosition(540f, screenHeight + ball.radius + 1f)
        assertTrue(ball.isBelow(screenHeight))
    }

    @Test
    fun `ball isBelow returns false when within screen`() {
        ball.setPosition(540f, screenHeight / 2f)
        assertFalse(ball.isBelow(screenHeight))
    }

    // AC-05-c — speed magnitude preserved after wall reflection
    @Test
    fun `speed magnitude preserved after left wall reflection`() {
        val speed = 400f
        ball.setPosition(ball.radius, 960f)
        ball.setVelocity(-speed, -300f)
        val magnitudeBefore = kotlin.math.sqrt(speed * speed + 300f * 300f)
        ball.update(0.016f)
        val magnitudeAfter = kotlin.math.sqrt(
            ball.velocity.x * ball.velocity.x + ball.velocity.y * ball.velocity.y
        )
        assertEquals(magnitudeBefore, magnitudeAfter, 1f)
    }
}
