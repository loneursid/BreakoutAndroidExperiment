package com.breakout

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

data class Vec2(val x: Float, val y: Float)

class Ball(private val screenWidth: Float, private val screenHeight: Float) {

    val radius: Float = screenWidth * Constants.BALL_RADIUS_FRACTION

    private var _position: Vec2 = Vec2(0f, 0f)
    private var _velocity: Vec2 = Vec2(0f, 0f)

    val position: Vec2 get() = _position
    val velocity: Vec2 get() = _velocity

    val left: Float get() = _position.x - radius
    val right: Float get() = _position.x + radius
    val top: Float get() = _position.y - radius
    val bottom: Float get() = _position.y + radius

    fun reset(paddleY: Float) {
        val speed = screenHeight * Constants.BALL_SPEED_FRACTION
        val angleRad = Constants.BALL_START_ANGLE_DEG * PI / 180.0
        _position = Vec2(
            screenWidth / 2f,
            paddleY - screenHeight * Constants.BALL_START_OFFSET_FRACTION
        )
        _velocity = Vec2(
            (speed * cos(angleRad)).toFloat(),
            -(speed * sin(angleRad)).toFloat()
        )
    }

    fun setPosition(x: Float, y: Float) {
        _position = Vec2(x, y)
    }

    fun setVelocity(vx: Float, vy: Float) {
        _velocity = Vec2(vx, vy)
    }

    fun update(deltaTime: Float) {
        _position = Vec2(
            _position.x + _velocity.x * deltaTime,
            _position.y + _velocity.y * deltaTime
        )
        reflectWalls()
    }

    private fun reflectWalls() {
        if (left <= 0f && _velocity.x < 0f) {
            _velocity = Vec2(-_velocity.x, _velocity.y)
            _position = Vec2(radius, _position.y)
        }
        if (right >= screenWidth && _velocity.x > 0f) {
            _velocity = Vec2(-_velocity.x, _velocity.y)
            _position = Vec2(screenWidth - radius, _position.y)
        }
        if (top <= 0f && _velocity.y < 0f) {
            _velocity = Vec2(_velocity.x, -_velocity.y)
            _position = Vec2(_position.x, radius)
        }
    }

    fun reflectOffPaddle(paddleCenterX: Float, paddleWidth: Float) {
        val offset = (_position.x - paddleCenterX) / (paddleWidth / 2f)
        val speed = screenHeight * Constants.BALL_SPEED_FRACTION
        val newVx = speed * offset * 0.8f
        val newVy = -kotlin.math.abs(_velocity.y)
        _velocity = Vec2(newVx, newVy)
    }

    fun reflectX() {
        _velocity = Vec2(-_velocity.x, _velocity.y)
    }

    fun reflectY() {
        _velocity = Vec2(_velocity.x, -_velocity.y)
    }

    fun isBelow(screenH: Float): Boolean = _position.y - radius > screenH
}
