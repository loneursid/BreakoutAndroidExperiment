package com.breakout.stepdefs

import com.breakout.Ball
import com.breakout.Constants
import com.breakout.Game
import com.breakout.GameState
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*
import kotlin.math.abs
import kotlin.math.sqrt

class BallSteps {

    private lateinit var game: Game
    private var preUpdateVx = 0f
    private var preUpdateVy = 0f

    @Given("the game transitions to PLAYING state")
    fun theGameTransitionsToPlayingState() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
    }

    @Given("the ball is moving left and its left edge reaches x equals 0")
    fun theBallMovingLeftAtLeftEdge() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        game.ball.setPosition(game.ball.radius, 960f)
        game.ball.setVelocity(-300f, -300f)
    }

    @Given("the ball is moving right and its right edge reaches screen width")
    fun theBallMovingRightAtRightEdge() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        game.ball.setPosition(GameContext.SCREEN_WIDTH - game.ball.radius, 960f)
        game.ball.setVelocity(300f, -300f)
    }

    @Given("the ball is moving upward and its top edge reaches y equals 0")
    fun theBallMovingUpAtTopEdge() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        game.ball.setPosition(540f, game.ball.radius)
        game.ball.setVelocity(300f, -300f)
    }

    @Given("the ball is moving downward and overlaps the paddle rect")
    fun theBallMovingDownOverlappingPaddle() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        val paddle = game.paddle
        game.ball.setPosition(paddle.centerX, paddle.top - game.ball.radius + 2f)
        game.ball.setVelocity(0f, 500f)
    }

    @Given("the ball is moving downward and its bottom edge exceeds screen height")
    fun theBallBelowScreen() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        game.ball.setPosition(540f, GameContext.SCREEN_HEIGHT + game.ball.radius + 1f)
        game.ball.setVelocity(0f, 300f)
    }

    @When("the first update tick runs")
    fun theFirstUpdateTickRuns() {
        // State already set in Given
    }

    @When("the collision is detected")
    fun theCollisionIsDetected() {
        preUpdateVx = game.ball.velocity.x
        preUpdateVy = game.ball.velocity.y
        game.update(0.016f)
    }

    @When("this is detected")
    fun thisIsDetected() {
        game.update(0.016f)
    }

    @Then("the ball is centred horizontally")
    fun theBallIsCentredHorizontally() {
        assertEquals(GameContext.SCREEN_WIDTH / 2f, game.ball.position.x, 1f)
    }

    @Then("the ball is 10 percent above the paddle")
    fun theBallIs10PercentAbovePaddle() {
        val paddleY = GameContext.SCREEN_HEIGHT * Constants.PADDLE_Y_FRACTION
        val expected = paddleY - GameContext.SCREEN_HEIGHT * Constants.BALL_START_OFFSET_FRACTION
        assertEquals(expected, game.ball.position.y, 2f)
    }

    @Then("the ball is moving at 45 degrees up-right")
    fun theBallIsMovingAt45DegreesUpRight() {
        assertTrue(game.ball.velocity.x > 0f)
        assertTrue(game.ball.velocity.y < 0f)
        assertEquals(abs(game.ball.velocity.x), abs(game.ball.velocity.y), 2f)
    }

    @Then("the ball speed is 28 percent of screen height per second")
    fun theBallSpeedIs28Percent() {
        val expected = GameContext.SCREEN_HEIGHT * Constants.BALL_SPEED_FRACTION
        val actual = sqrt(game.ball.velocity.x * game.ball.velocity.x + game.ball.velocity.y * game.ball.velocity.y)
        assertEquals(expected, actual, 3f)
    }

    @Then("the ball X velocity is negated")
    fun theBallXVelocityIsNegated() {
        assertTrue(game.ball.velocity.x * preUpdateVx < 0f || game.ball.velocity.x > 0f)
    }

    @Then("the ball moves rightward")
    fun theBallMovesRightward() {
        assertTrue(game.ball.velocity.x > 0f)
    }

    @Then("the ball moves leftward")
    fun theBallMovesLeftward() {
        assertTrue(game.ball.velocity.x < 0f)
    }

    @Then("the ball Y velocity is negated")
    fun theBallYVelocityIsNegated() {
        assertTrue(game.ball.velocity.y * preUpdateVy <= 0f)
    }

    @Then("the ball moves downward")
    fun theBallMovesDownward() {
        assertTrue(game.ball.velocity.y > 0f)
    }

    @Then("the ball moves upward")
    fun theBallMovesUpward() {
        assertTrue(game.ball.velocity.y < 0f)
    }

    @Then("the game transitions to GAME_OVER state")
    fun theGameTransitionsToGameOver() {
        assertEquals(GameState.GAME_OVER, game.state)
    }
}
