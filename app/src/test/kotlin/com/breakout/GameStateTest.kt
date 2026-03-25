package com.breakout

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameStateTest {

    private lateinit var game: Game
    private lateinit var audioManager: AudioManager
    private val screenWidth = 1080f
    private val screenHeight = 1920f

    @BeforeEach
    fun setUp() {
        audioManager = mockk(relaxed = true)
        game = Game(screenWidth, screenHeight, audioManager)
    }

    // AC-01-c
    @Test
    fun `tap on START transitions to PLAYING`() {
        assertEquals(GameState.START, game.state)
        game.onTap()
        assertEquals(GameState.PLAYING, game.state)
    }

    // AC-04-a
    @Test
    fun `playing state has 40 active bricks`() {
        game.onTap()
        assertEquals(40, game.activeBrickCount())
    }

    // AC-06-a
    @Test
    fun `score is 0 at start of playing`() {
        game.onTap()
        assertEquals(0, game.scoreManager.score)
    }

    // AC-03-f / AC-07-a
    @Test
    fun `ball below screen triggers GAME_OVER`() {
        game.onTap()
        game.ball.setPosition(540f, screenHeight + 100f)
        game.update(0.016f)
        assertEquals(GameState.GAME_OVER, game.state)
    }

    // AC-07-c
    @Test
    fun `tap on GAME_OVER returns to START`() {
        game.onTap()
        game.ball.setPosition(540f, screenHeight + 100f)
        game.update(0.016f)
        assertEquals(GameState.GAME_OVER, game.state)
        game.onTap()
        assertEquals(GameState.START, game.state)
    }

    // AC-08-a
    @Test
    fun `destroying all bricks transitions to WIN`() {
        game.onTap()
        game.bricks.forEach { it.isActive = false }
        // Re-create one active brick to trigger via update
        // Instead: directly force win by making all bricks inactive then updating
        game.update(0.016f)
        assertEquals(GameState.WIN, game.state)
    }

    // AC-08-c
    @Test
    fun `tap on WIN returns to START`() {
        game.onTap()
        game.bricks.forEach { it.isActive = false }
        game.update(0.016f)
        assertEquals(GameState.WIN, game.state)
        game.onTap()
        assertEquals(GameState.START, game.state)
    }

    // AC-07-a: audio plays on ball lost
    @Test
    fun `ball lost plays BALL_LOST sound`() {
        game.onTap()
        game.ball.setPosition(540f, screenHeight + 100f)
        game.update(0.016f)
        verify { audioManager.play(SoundEvent.BALL_LOST) }
    }

    // AC-10-f: win plays WIN sound
    @Test
    fun `win condition plays WIN sound`() {
        game.onTap()
        game.bricks.forEach { it.isActive = false }
        game.update(0.016f)
        verify { audioManager.play(SoundEvent.WIN) }
    }
}
