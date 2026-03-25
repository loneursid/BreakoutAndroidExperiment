package com.breakout.stepdefs

import com.breakout.AudioManager
import com.breakout.Game
import io.mockk.mockk

/**
 * Shared test context for step definitions that need direct Game access
 * (unit-level BDD steps not requiring a running Activity).
 */
object GameContext {
    const val SCREEN_WIDTH = 1080f
    const val SCREEN_HEIGHT = 1920f

    fun newGame(): Pair<Game, AudioManager> {
        val audio = mockk<AudioManager>(relaxed = true)
        return Game(SCREEN_WIDTH, SCREEN_HEIGHT, audio) to audio
    }
}
