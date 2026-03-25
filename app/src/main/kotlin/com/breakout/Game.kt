package com.breakout

enum class GameState {
    START, PLAYING, GAME_OVER, WIN
}

class Game(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val audioManager: AudioManager
) {
    val ball = Ball(screenWidth, screenHeight)
    val paddle = Paddle(screenWidth, screenHeight)
    private var _bricks: List<Brick> = emptyList()
    val bricks: List<Brick> get() = _bricks

    val scoreManager = ScoreManager()
    private val collisionDetector = CollisionDetector()

    private var _state: GameState = GameState.START
    val state: GameState get() = _state

    init {
        resetEntities()
    }

    private fun resetEntities() {
        paddle.reset()
        ball.reset(paddle.y)
        _bricks = Brick.createGrid(screenWidth, screenHeight)
        scoreManager.reset()
    }

    fun onTap() {
        when (_state) {
            GameState.START -> _state = GameState.PLAYING
            GameState.GAME_OVER, GameState.WIN -> {
                resetEntities()
                _state = GameState.START
            }
            GameState.PLAYING -> {}
        }
    }

    fun onTouchLeft(deltaTime: Float) {
        if (_state == GameState.PLAYING) paddle.moveLeft(deltaTime)
    }

    fun onTouchRight(deltaTime: Float) {
        if (_state == GameState.PLAYING) paddle.moveRight(deltaTime)
    }

    fun update(deltaTime: Float) {
        if (_state != GameState.PLAYING) return

        ball.update(deltaTime)

        // Ball vs paddle
        if (collisionDetector.checkBallPaddle(ball, paddle)) {
            ball.reflectOffPaddle(paddle.centerX, paddle.width)
            audioManager.play(SoundEvent.PADDLE_HIT)
        }

        // Ball vs bricks
        for (brick in _bricks) {
            if (!brick.isActive) continue
            val side = collisionDetector.checkBallBrick(ball, brick)
            if (side != CollisionDetector.CollisionSide.NONE) {
                brick.isActive = false
                scoreManager.addBrickDestroyed()
                audioManager.play(SoundEvent.BRICK_HIT)
                when (side) {
                    CollisionDetector.CollisionSide.LEFT,
                    CollisionDetector.CollisionSide.RIGHT -> ball.reflectX()
                    CollisionDetector.CollisionSide.TOP,
                    CollisionDetector.CollisionSide.BOTTOM -> ball.reflectY()
                    CollisionDetector.CollisionSide.NONE -> {}
                }
                break  // one brick per frame
            }
        }

        // Win check
        if (_bricks.none { it.isActive }) {
            audioManager.play(SoundEvent.WIN)
            _state = GameState.WIN
            return
        }

        // Ball exits bottom
        if (ball.isBelow(screenHeight)) {
            audioManager.play(SoundEvent.BALL_LOST)
            _state = GameState.GAME_OVER
        }
    }

    fun activeBrickCount(): Int = _bricks.count { it.isActive }
}
