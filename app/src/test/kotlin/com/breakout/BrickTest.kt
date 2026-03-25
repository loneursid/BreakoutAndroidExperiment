package com.breakout

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BrickTest {

    private val screenWidth = 1080f
    private val screenHeight = 1920f

    // AC-04-a
    @Test
    fun `grid creates exactly 40 bricks`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        assertEquals(40, bricks.size)
    }

    @Test
    fun `all bricks start active`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        assertTrue(bricks.all { it.isActive })
    }

    // AC-04-b
    @Test
    fun `first row is red`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        val firstRow = bricks.take(Constants.BRICK_COLS)
        assertTrue(firstRow.all { it.color == Constants.COLOR_RED })
    }

    @Test
    fun `second row is orange`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        val secondRow = bricks.drop(Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(secondRow.all { it.color == Constants.COLOR_ORANGE })
    }

    @Test
    fun `third row is yellow`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        val row = bricks.drop(2 * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(row.all { it.color == Constants.COLOR_YELLOW })
    }

    @Test
    fun `fourth row is green`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        val row = bricks.drop(3 * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(row.all { it.color == Constants.COLOR_GREEN })
    }

    @Test
    fun `fifth row is blue`() {
        val bricks = Brick.createGrid(screenWidth, screenHeight)
        val row = bricks.drop(4 * Constants.BRICK_COLS).take(Constants.BRICK_COLS)
        assertTrue(row.all { it.color == Constants.COLOR_BLUE })
    }

    @Test
    fun `brick can be deactivated`() {
        val brick = Brick(0f, 0f, 100f, 50f, Constants.COLOR_RED)
        brick.isActive = false
        assertFalse(brick.isActive)
    }
}
