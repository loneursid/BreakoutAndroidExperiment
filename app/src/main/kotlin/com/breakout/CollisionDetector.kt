package com.breakout

import kotlin.math.abs

class CollisionDetector {

    fun checkBallPaddle(ball: Ball, paddle: Paddle): Boolean {
        if (ball.velocity.y <= 0f) return false  // moving upward, skip
        return circleIntersectsRect(
            ball.position.x, ball.position.y, ball.radius,
            paddle.left, paddle.top, paddle.right, paddle.bottom
        )
    }

    fun checkBallBrick(ball: Ball, brick: Brick): CollisionSide {
        if (!brick.isActive) return CollisionSide.NONE
        if (!circleIntersectsRect(
                ball.position.x, ball.position.y, ball.radius,
                brick.left, brick.top, brick.right, brick.bottom
            )
        ) return CollisionSide.NONE

        return determineSide(
            ball.position.x, ball.position.y, ball.radius,
            brick.left, brick.top, brick.right, brick.bottom
        )
    }

    private fun circleIntersectsRect(
        cx: Float, cy: Float, r: Float,
        rectLeft: Float, rectTop: Float, rectRight: Float, rectBottom: Float
    ): Boolean {
        val nearestX = cx.coerceIn(rectLeft, rectRight)
        val nearestY = cy.coerceIn(rectTop, rectBottom)
        val dx = cx - nearestX
        val dy = cy - nearestY
        return (dx * dx + dy * dy) <= r * r
    }

    private fun determineSide(
        cx: Float, cy: Float, r: Float,
        rectLeft: Float, rectTop: Float, rectRight: Float, rectBottom: Float
    ): CollisionSide {
        val overlapLeft = cx + r - rectLeft
        val overlapRight = rectRight - (cx - r)
        val overlapTop = cy + r - rectTop
        val overlapBottom = rectBottom - (cy - r)

        val minOverlap = minOf(overlapLeft, overlapRight, overlapTop, overlapBottom)
        return when (minOverlap) {
            overlapLeft -> CollisionSide.LEFT
            overlapRight -> CollisionSide.RIGHT
            overlapTop -> CollisionSide.TOP
            else -> CollisionSide.BOTTOM
        }
    }

    enum class CollisionSide {
        NONE, LEFT, RIGHT, TOP, BOTTOM
    }
}
