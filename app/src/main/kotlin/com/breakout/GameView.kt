package com.breakout

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    @Volatile private var isRunning = false
    private var thread: Thread? = null

    @Volatile private var game: Game? = null
    @Volatile private var renderer: Renderer? = null
    private val audioManager = AudioManager(context)

    @Volatile private var touchingLeft = false
    @Volatile private var touchingRight = false
    @Volatile private var isTap = false

    init {
        holder.addCallback(this)
    }

    // surfaceChanged gives guaranteed non-zero dimensions; use it for (re-)init
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (width == 0 || height == 0) return
        val wasRunning = isRunning
        if (wasRunning) stopLoop()
        game = Game(width.toFloat(), height.toFloat(), audioManager)
        renderer = Renderer(width.toFloat(), height.toFloat())
        startLoop()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Dimensions may be 0 here; real init happens in surfaceChanged
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopLoop()
    }

    private fun startLoop() {
        isRunning = true
        thread = Thread(this, "GameThread").apply { start() }
    }

    fun stopLoop() {
        isRunning = false
        thread?.join(2000)
        thread = null
    }

    override fun run() {
        var lastTime = System.nanoTime()
        while (isRunning) {
            val now = System.nanoTime()
            val deltaTime = ((now - lastTime) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
            lastTime = now

            val g = game ?: run { Thread.sleep(16); null } ?: continue
            val r = renderer ?: continue

            if (isTap) {
                isTap = false
                g.onTap()
            }
            if (touchingLeft) g.onTouchLeft(deltaTime)
            if (touchingRight) g.onTouchRight(deltaTime)

            g.update(deltaTime)

            val canvas = holder.lockCanvas() ?: continue
            try {
                r.render(canvas, g, audioManager.isMuted)
            } finally {
                holder.unlockCanvasAndPost(canvas)
            }

            val elapsed = System.nanoTime() - now
            val sleepNs = Constants.FRAME_TIME_NS - elapsed
            if (sleepNs > 0) {
                try {
                    Thread.sleep(sleepNs / 1_000_000, (sleepNs % 1_000_000).toInt())
                } catch (_: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val g = game ?: return true

        val muteButtonSize = dpToPx(Constants.MUTE_BUTTON_DP)
        val muteButtonRight = width.toFloat()
        val muteButtonLeft = muteButtonRight - muteButtonSize
        val muteButtonBottom = muteButtonSize

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                if (x >= muteButtonLeft && y <= muteButtonBottom) {
                    audioManager.toggleMute()
                    return true
                }
                isTap = true
                touchingLeft = x < width / 2f
                touchingRight = x >= width / 2f
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                touchingLeft = x < width / 2f
                touchingRight = x >= width / 2f
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                touchingLeft = false
                touchingRight = false
            }
        }
        return true
    }

    private fun dpToPx(dp: Float): Float =
        dp * resources.displayMetrics.density
}
