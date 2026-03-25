package com.breakout

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScoreManagerTest {

    private lateinit var scoreManager: ScoreManager

    @BeforeEach
    fun setUp() {
        scoreManager = ScoreManager()
    }

    // AC-06-a
    @Test
    fun `score starts at zero`() {
        assertEquals(0, scoreManager.score)
    }

    // AC-06-b
    @Test
    fun `score increases by 10 per brick`() {
        scoreManager.addBrickDestroyed()
        assertEquals(10, scoreManager.score)
    }

    @Test
    fun `score accumulates correctly`() {
        repeat(5) { scoreManager.addBrickDestroyed() }
        assertEquals(50, scoreManager.score)
    }

    @Test
    fun `reset returns score to zero`() {
        repeat(3) { scoreManager.addBrickDestroyed() }
        scoreManager.reset()
        assertEquals(0, scoreManager.score)
    }

    @Test
    fun `format includes score value`() {
        scoreManager.addBrickDestroyed()
        assertTrue(scoreManager.format().contains("10"))
    }
}
