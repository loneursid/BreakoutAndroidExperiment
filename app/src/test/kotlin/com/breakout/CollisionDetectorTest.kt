package com.breakout

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CollisionDetectorTest {

    private lateinit var detector: CollisionDetector
    private val screenWidth = 1080f
    private val screenHeight = 1920f

    @BeforeEach
    fun setUp() {
        detector = CollisionDetector()
    }

    // AC-05-b
    @Test
    fun `ball hits paddle from above`() {
        val paddle = Paddle(screenWidth, screenHeight)
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(paddle.centerX, paddle.top - ball.radius + 1f)
        ball.setVelocity(0f, 300f)  // moving down
        assertTrue(detector.checkBallPaddle(ball, paddle))
    }

    @Test
    fun `ball moving up does not register paddle collision`() {
        val paddle = Paddle(screenWidth, screenHeight)
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(paddle.centerX, paddle.top - ball.radius + 1f)
        ball.setVelocity(0f, -300f)  // moving up
        assertFalse(detector.checkBallPaddle(ball, paddle))
    }

    @Test
    fun `ball far from paddle returns no collision`() {
        val paddle = Paddle(screenWidth, screenHeight)
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(100f, 100f)
        ball.setVelocity(0f, 300f)
        assertFalse(detector.checkBallPaddle(ball, paddle))
    }

    // AC-05-a
    @Test
    fun `ball hitting brick from top returns TOP side`() {
        val brickX = 100f
        val brickY = 200f
        val brick = Brick(brickX, brickY, 100f, 40f, Constants.COLOR_RED)
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(brickX + 50f, brickY - ball.radius + 1f)
        ball.setVelocity(0f, 300f)
        val side = detector.checkBallBrick(ball, brick)
        assertEquals(CollisionDetector.CollisionSide.TOP, side)
    }

    @Test
    fun `ball hitting brick from bottom returns BOTTOM side`() {
        val brickX = 100f
        val brickY = 200f
        val brick = Brick(brickX, brickY, 100f, 40f, Constants.COLOR_RED)
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(brickX + 50f, brickY + 40f + ball.radius - 1f)
        ball.setVelocity(0f, -300f)
        val side = detector.checkBallBrick(ball, brick)
        assertEquals(CollisionDetector.CollisionSide.BOTTOM, side)
    }

    @Test
    fun `inactive brick returns no collision`() {
        val brick = Brick(100f, 200f, 100f, 40f, Constants.COLOR_RED)
        brick.isActive = false
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(150f, 220f)
        ball.setVelocity(0f, 300f)
        val side = detector.checkBallBrick(ball, brick)
        assertEquals(CollisionDetector.CollisionSide.NONE, side)
    }

    @Test
    fun `ball not overlapping brick returns no collision`() {
        val brick = Brick(100f, 200f, 100f, 40f, Constants.COLOR_RED)
        val ball = Ball(screenWidth, screenHeight)
        ball.setPosition(500f, 500f)
        ball.setVelocity(0f, 300f)
        val side = detector.checkBallBrick(ball, brick)
        assertEquals(CollisionDetector.CollisionSide.NONE, side)
    }
}
