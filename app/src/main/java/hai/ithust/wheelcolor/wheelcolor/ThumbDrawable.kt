package hai.ithust.wheelcolor.wheelcolor

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

internal class ThumbDrawable {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10f
    }

    private var x = 0f
    private var y = 0f

    var indicatorColor = 0
    var strokeColor = 0
    var radius = 0

    fun setCoordinates(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun draw(canvas: Canvas) {
        drawStroke(canvas)
        drawColorIndicator(canvas)
    }

    private fun drawStroke(canvas: Canvas) {
        val strokeCircleRadius = radius

        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(x, y, strokeCircleRadius.toFloat(), paint)
    }

    private fun drawColorIndicator(canvas: Canvas) {
        val colorIndicatorCircleRadius = radius.toFloat() - paint.strokeWidth / 2

        paint.color = indicatorColor
        paint.style = Paint.Style.FILL
        paint.setShadowLayer(radius.toFloat(), 0f, 0f, Color.BLACK)
        canvas.drawCircle(x, y, colorIndicatorCircleRadius, paint)
    }

    fun restoreState(state: ThumbDrawableState) {
        radius = state.radius
        strokeColor = state.strokeColor
    }

    fun saveState() = ThumbDrawableState(this)
}
