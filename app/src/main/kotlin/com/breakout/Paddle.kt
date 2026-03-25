package com.breakout

class Paddle(private val screenWidth: Float, private val screenHeight: Float) {

    val width: Float = screenWidth * Constants.PADDLE_WIDTH_FRACTION
    val height: Float = screenHeight * Constants.PADDLE_HEIGHT_FRACTION
    val y: Float = screenHeight * Constants.PADDLE_Y_FRACTION

    private var _x: Float = (screenWidth - width) / 2f
    val x: Float get() = _x

    val centerX: Float get() = _x + width / 2f
    val left: Float get() = _x
    val right: Float get() = _x + width
    val top: Float get() = y
    val bottom: Float get() = y + height

    private val speed: Float = screenWidth * Constants.PADDLE_SPEED_FRACTION

    fun reset() {
        _x = (screenWidth - width) / 2f
    }

    fun moveLeft(deltaTime: Float) {
        _x = (_x - speed * deltaTime).coerceAtLeast(0f)
    }

    fun moveRight(deltaTime: Float) {
        _x = (_x + speed * deltaTime).coerceAtMost(screenWidth - width)
    }

    fun setX(x: Float) {
        _x = x.coerceIn(0f, screenWidth - width)
    }
}
