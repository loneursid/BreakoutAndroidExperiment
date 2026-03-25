package com.breakout

class Brick(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val color: Int
) {
    var isActive: Boolean = true

    val left: Float get() = x
    val right: Float get() = x + width
    val top: Float get() = y
    val bottom: Float get() = y + height

    companion object {
        val ROW_COLORS = intArrayOf(
            Constants.COLOR_RED,
            Constants.COLOR_ORANGE,
            Constants.COLOR_YELLOW,
            Constants.COLOR_GREEN,
            Constants.COLOR_BLUE
        )

        fun createGrid(screenWidth: Float, screenHeight: Float): List<Brick> {
            val margin = screenWidth * Constants.BRICK_HORIZONTAL_MARGIN_FRACTION
            val totalWidth = screenWidth - 2 * margin
            val gap = Constants.BRICK_GAP_PX
            val brickWidth = (totalWidth - gap * (Constants.BRICK_COLS - 1)) / Constants.BRICK_COLS
            val brickHeight = screenHeight * Constants.BRICK_HEIGHT_FRACTION
            val topMargin = screenHeight * Constants.BRICK_TOP_MARGIN_FRACTION

            return buildList {
                for (row in 0 until Constants.BRICK_ROWS) {
                    val color = ROW_COLORS[row]
                    for (col in 0 until Constants.BRICK_COLS) {
                        val bx = margin + col * (brickWidth + gap)
                        val by = topMargin + row * (brickHeight + gap)
                        add(Brick(bx, by, brickWidth, brickHeight, color))
                    }
                }
            }
        }
    }
}
