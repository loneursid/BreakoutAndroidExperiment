package com.breakout.stepdefs

import com.breakout.Brick
import com.breakout.Constants
import com.breakout.Game
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.*

class BrickSteps {

    private lateinit var game: Game
    private var scoreBefore = 0

    @Given("the brick grid renders")
    fun theBrickGridRenders() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
    }

    @Given("an active brick exists and the ball overlaps its bounds")
    fun anActiveBrickExistsAndBallOverlaps() {
        val (g, _) = GameContext.newGame()
        game = g
        game.onTap()
        val brick = game.bricks.first { it.isActive }
        game.ball.setPosition(
            brick.x + brick.width / 2f,
            brick.bottom + game.ball.radius - 2f
        )
        game.ball.setVelocity(0f, -500f)
        scoreBefore = game.scoreManager.score
    }

    @When("the brick grid initialises")
    fun theBrickGridInitialises() {
        // Already done in Given
    }

    @When("rows are drawn top to bottom")
    fun rowsAreDrawnTopToBottom() {
        // Already rendered
    }

    @When("the collision is processed")
    fun theCollisionIsProcessed() {
        game.update(0.016f)
    }

    @Then("exactly 40 bricks are active and visible")
    fun exactly40BricksAreActive() {
        assertEquals(40, game.activeBrickCount())
    }

    @Then("row {int} is red")
    fun row1IsRed(row: Int) {
        val bricks = game.bricks
        val rowBricks = bricks.drop((row - 1) * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(rowBricks.all { it.color == Constants.COLOR_RED })
    }

    @Then("row {int} is orange")
    fun row2IsOrange(row: Int) {
        val bricks = game.bricks
        val rowBricks = bricks.drop((row - 1) * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(rowBricks.all { it.color == Constants.COLOR_ORANGE })
    }

    @Then("row {int} is yellow")
    fun row3IsYellow(row: Int) {
        val bricks = game.bricks
        val rowBricks = bricks.drop((row - 1) * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(rowBricks.all { it.color == Constants.COLOR_YELLOW })
    }

    @Then("row {int} is green")
    fun row4IsGreen(row: Int) {
        val bricks = game.bricks
        val rowBricks = bricks.drop((row - 1) * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(rowBricks.all { it.color == Constants.COLOR_GREEN })
    }

    @Then("row {int} is blue")
    fun row5IsBlue(row: Int) {
        val bricks = game.bricks
        val rowBricks = bricks.drop((row - 1) * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(rowBricks.all { it.color == Constants.COLOR_BLUE })
    }

    @Then("the brick is marked inactive")
    fun theBrickIsMarkedInactive() {
        val destroyed = game.bricks.count { !it.isActive }
        assertTrue(destroyed > 0)
    }

    @Then("{int} points are added to the score")
    fun pointsAreAddedToScore(points: Int) {
        assertEquals(scoreBefore + points, game.scoreManager.score)
    }
}
