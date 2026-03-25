package com.breakout.stepdefs

import com.breakout.Game
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*

class ScoringSteps {

    private lateinit var game: Game
    private var scoreBefore = 0

    @Given("a new game starts")
    fun aNewGameStarts() {
        val (g, _) = GameContext.newGame()
        game = g
    }

    @When("PLAYING state is entered")
    fun playingStateIsEntered() {
        game.onTap()
    }

    @When("a brick is destroyed")
    fun aBrickIsDestroyed() {
        scoreBefore = game.scoreManager.score
        val brick = game.bricks.first { it.isActive }
        game.ball.setPosition(brick.x + brick.width / 2f, brick.bottom + game.ball.radius - 2f)
        game.ball.setVelocity(0f, -500f)
        game.update(0.016f)
    }

    @When("any frame renders")
    fun anyFrameRenders() {
        game.update(0.016f)
    }

    @Then("score is initialised to 0")
    fun scoreIsInitialisedToZero() {
        assertEquals(0, game.scoreManager.score)
    }

    @Then("score increases by exactly 10 points")
    fun scoreIncreasesBy10Points() {
        assertEquals(scoreBefore + 10, game.scoreManager.score)
    }

    @Then("the current score is visible in the top-left HUD")
    fun scoreIsVisibleInHud() {
        val formatted = game.scoreManager.format()
        assertTrue(formatted.contains(game.scoreManager.score.toString()))
    }
}
