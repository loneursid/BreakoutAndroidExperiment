package com.breakout

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Renderer(private val screenWidth: Float, private val screenHeight: Float) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val hudTextSize = screenHeight * Constants.HUD_TEXT_SIZE_FRACTION
    private val hudPadding = screenWidth * Constants.HUD_PADDING_FRACTION

    fun render(canvas: Canvas, game: Game, isMuted: Boolean) {
        canvas.drawColor(Color.BLACK)
        when (game.state) {
            GameState.START -> drawStart(canvas)
            GameState.PLAYING -> drawPlaying(canvas, game, isMuted)
            GameState.GAME_OVER -> drawGameOver(canvas, game.scoreManager.score)
            GameState.WIN -> drawWin(canvas, game.scoreManager.score)
        }
        drawMuteButton(canvas, isMuted)
    }

    private fun drawPlaying(canvas: Canvas, game: Game, isMuted: Boolean) {
        drawBricks(canvas, game.bricks)
        drawPaddle(canvas, game.paddle)
        drawBall(canvas, game.ball)
        drawHudScore(canvas, game.scoreManager.format())
    }

    private fun drawBricks(canvas: Canvas, bricks: List<Brick>) {
        val rect = RectF()
        for (brick in bricks) {
            if (!brick.isActive) continue
            paint.color = brick.color
            rect.set(brick.left, brick.top, brick.right, brick.bottom)
            canvas.drawRect(rect, paint)
        }
    }

    private fun drawPaddle(canvas: Canvas, paddle: Paddle) {
        paint.color = Color.WHITE
        canvas.drawRect(paddle.left, paddle.top, paddle.right, paddle.bottom, paint)
    }

    private fun drawBall(canvas: Canvas, ball: Ball) {
        paint.color = Color.WHITE
        canvas.drawCircle(ball.position.x, ball.position.y, ball.radius, paint)
    }

    private fun drawHudScore(canvas: Canvas, scoreText: String) {
        paint.color = Color.WHITE
        paint.textSize = hudTextSize
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(scoreText, hudPadding, hudPadding + hudTextSize, paint)
    }

    private fun drawStart(canvas: Canvas) {
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = screenHeight * 0.08f
        canvas.drawText("BREAKOUT", screenWidth / 2f, screenHeight * 0.40f, paint)
        paint.textSize = screenHeight * 0.04f
        canvas.drawText("Tap to start", screenWidth / 2f, screenHeight * 0.55f, paint)
    }

    private fun drawGameOver(canvas: Canvas, score: Int) {
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = screenHeight * 0.08f
        canvas.drawText("Game Over", screenWidth / 2f, screenHeight * 0.38f, paint)
        paint.textSize = screenHeight * 0.05f
        canvas.drawText("Score: $score", screenWidth / 2f, screenHeight * 0.50f, paint)
        paint.textSize = screenHeight * 0.04f
        canvas.drawText("Tap to restart", screenWidth / 2f, screenHeight * 0.62f, paint)
    }

    private fun drawWin(canvas: Canvas, score: Int) {
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = screenHeight * 0.08f
        canvas.drawText("You Win!", screenWidth / 2f, screenHeight * 0.38f, paint)
        paint.color = Color.WHITE
        paint.textSize = screenHeight * 0.05f
        canvas.drawText("Score: $score", screenWidth / 2f, screenHeight * 0.50f, paint)
        paint.textSize = screenHeight * 0.04f
        canvas.drawText("Tap to restart", screenWidth / 2f, screenHeight * 0.62f, paint)
    }

    private fun drawMuteButton(canvas: Canvas, isMuted: Boolean) {
        val label = if (isMuted) "\uD83D\uDD07" else "\uD83D\uDD0A"  // 🔇 or 🔊
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = hudTextSize
        canvas.drawText(label, screenWidth - hudPadding, hudPadding + hudTextSize, paint)
    }
}
