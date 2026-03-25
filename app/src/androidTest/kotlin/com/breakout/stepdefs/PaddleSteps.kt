package com.breakout.stepdefs

import com.breakout.Constants
import com.breakout.Game
import com.breakout.GameState
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*

class PaddleSteps {

    private lateinit var game: Game

    @Given("the game enters PLAYING state")
    fun theGameEntersPlayingState() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
    }

    @Given("the game is in PLAYING state")
    fun theGameIsInPlayingState() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
    }

    @Given("the paddle is not at the left edge")
    fun thePaddleIsNotAtTheLeftEdge() {
        // Paddle starts centred — not at left edge by default
    }

    @Given("the paddle is not at the right edge")
    fun thePaddleIsNotAtTheRightEdge() {
        // Paddle starts centred
    }

    @Given("the paddle is at the left screen edge")
    fun thePaddleIsAtTheLeftScreenEdge() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        game.paddle.setX(0f)
    }

    @Given("the paddle is at the right screen edge")
    fun thePaddleIsAtTheRightScreenEdge() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        game.paddle.setX(GameContext.SCREEN_WIDTH - game.paddle.width)
    }

    @When("the first frame renders")
    fun theFirstFrameRenders() {
        // Initial state after entering PLAYING
    }

    @When("the player holds the left half of the screen for 1 second")
    fun thePlayerHoldsLeftHalfForOneSecond() {
        game.onTouchLeft(1.0f)
    }

    @When("the player holds the right half of the screen for 1 second")
    fun thePlayerHoldsRightHalfForOneSecond() {
        game.onTouchRight(1.0f)
    }

    @When("the player holds the left half of the screen")
    fun thePlayerHoldsLeftHalf() {
        game.onTouchLeft(1.0f)
    }

    @When("the player holds the right half of the screen")
    fun thePlayerHoldsRightHalf() {
        game.onTouchRight(1.0f)
    }

    @Then("the paddle is centred horizontally")
    fun thePaddleIsCentredHorizontally() {
        val expectedX = (GameContext.SCREEN_WIDTH - game.paddle.width) / 2f
        assertEquals(expectedX, game.paddle.x, 1f)
    }

    @Then("the paddle is at 88 percent screen height")
    fun thePaddleIsAt88PercentScreenHeight() {
        val expectedY = GameContext.SCREEN_HEIGHT * Constants.PADDLE_Y_FRACTION
        assertEquals(expectedY, game.paddle.y, 1f)
    }

    @Then("the paddle width is 22 percent of screen width")
    fun thePaddleWidthIs22PercentOfScreenWidth() {
        val expected = GameContext.SCREEN_WIDTH * Constants.PADDLE_WIDTH_FRACTION
        assertEquals(expected, game.paddle.width, 1f)
    }

    @Then("the paddle has moved left by 45 percent of screen width")
    fun thePaddleHasMovedLeftBy45Percent() {
        val expectedDelta = GameContext.SCREEN_WIDTH * Constants.PADDLE_SPEED_FRACTION
        val startX = (GameContext.SCREEN_WIDTH - game.paddle.width) / 2f
        assertEquals(startX - expectedDelta, game.paddle.x, 2f)
    }

    @Then("the paddle has moved right by 45 percent of screen width")
    fun thePaddleHasMovedRightBy45Percent() {
        val expectedDelta = GameContext.SCREEN_WIDTH * Constants.PADDLE_SPEED_FRACTION
        val startX = (GameContext.SCREEN_WIDTH - game.paddle.width) / 2f
        assertEquals(startX + expectedDelta, game.paddle.x, 2f)
    }

    @Then("the paddle position does not decrease below zero")
    fun thePaddlePositionDoesNotDecreaseBelowZero() {
        assertEquals(0f, game.paddle.x, 0.1f)
    }

    @Then("the paddle position does not exceed screen width minus paddle width")
    fun thePaddlePositionDoesNotExceedRightBound() {
        val maxX = GameContext.SCREEN_WIDTH - game.paddle.width
        assertEquals(maxX, game.paddle.x, 0.1f)
    }
}
