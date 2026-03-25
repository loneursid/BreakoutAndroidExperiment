package com.breakout

class ScoreManager {
    private var _score: Int = 0
    val score: Int get() = _score

    fun reset() {
        _score = 0
    }

    fun addBrickDestroyed() {
        _score += Constants.BRICK_POINTS
    }

    fun format(): String = "Score: $_score"
}
