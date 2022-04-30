package hai.ithust.wheelcolor.wheelcolor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import hai.ithust.wheelcolor.R
import kotlin.math.*

private val HUE_COLORS = intArrayOf(
    Color.RED,
    Color.YELLOW,
    Color.GREEN,
    Color.CYAN,
    Color.BLUE,
    Color.MAGENTA,
    Color.RED
)

private val SATURATION_COLORS = intArrayOf(
    Color.WHITE,
    ColorUtils.setAlphaComponent(Color.WHITE, 0)
)

class ColorWheel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val hueGradient = GradientDrawable().apply {
        gradientType = GradientDrawable.SWEEP_GRADIENT
        shape = GradientDrawable.OVAL
        colors = HUE_COLORS
    }

    private val saturationGradient = GradientDrawable().apply {
        gradientType = GradientDrawable.RADIAL_GRADIENT
        shape = GradientDrawable.OVAL
        colors = SATURATION_COLORS
    }

    private val brightnessOverlayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        alpha = 0
    }

    private val thumbDrawable = ThumbDrawable()
    private val hsvColor = HsvColor(value = 1f)

    private var wheelCenterX = 0
    private var wheelCenterY = 0
    private var wheelRadius = 0

    var brightness: Float = 0.0f
        set(value) {
            field = value
            brightnessOverlayPaint.alpha = (255 * value / 100f).roundToInt()
            hsvColor.apply {
                this.value = 1f - value / 100
            }
            invalidate()
        }

    var rgb
        get() = hsvColor.rgb
        private set(rgb) {
            hsvColor.rgb = rgb
            brightness = 1f - hsvColor.value
            fireColorListener()
            invalidate()
        }

    var colorChangeListener: ((Int) -> Unit)? = null

    init {
        parseAttributes(context, attrs)
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.ColorWheel,
            0,
            R.style.ColorWheelDefaultStyle
        )
        thumbDrawable.radius = array.getDimensionPixelSize(R.styleable.ColorWheel_cw_thumbRadius, 0)
        thumbDrawable.strokeColor = array.getColor(R.styleable.ColorWheel_cw_thumbStrokeColor, 0)
        array.recycle()
    }

    fun setSelectedColor(selectedColor: Int) {
        rgb = selectedColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateArea()
    }

    override fun onDraw(canvas: Canvas) {
        drawColorWheel(canvas)
        drawThumb(canvas)
    }

    private fun calculateArea() {
        val hSpace = width - paddingLeft - paddingRight
        val vSpace = height - paddingTop - paddingBottom

        wheelCenterX = paddingLeft + hSpace / 2
        wheelCenterY = paddingTop + vSpace / 2
        wheelRadius = maxOf(minOf(hSpace, vSpace) / 2, 0)

        val left = wheelCenterX - wheelRadius
        val top = wheelCenterY - wheelRadius
        val right = wheelCenterX + wheelRadius
        val bottom = wheelCenterY + wheelRadius

        hueGradient.setBounds(left, top, right, bottom)
        saturationGradient.setBounds(left, top, right, bottom)
        saturationGradient.gradientRadius = wheelRadius.toFloat()
    }

    private fun drawColorWheel(canvas: Canvas) {
        hueGradient.draw(canvas)
        saturationGradient.draw(canvas)

        canvas.drawCircle(
            wheelCenterX.toFloat(),
            wheelCenterY.toFloat(),
            wheelRadius.toFloat(),
            brightnessOverlayPaint
        )
    }

    private fun drawThumb(canvas: Canvas) {
        val r = hsvColor.saturation * wheelRadius
        val hueRadians = Math.toRadians(hsvColor.hue.toDouble()).toFloat()
        val x = cos(hueRadians) * r + wheelCenterX
        val y = sin(hueRadians) * r + wheelCenterY

        thumbDrawable.indicatorColor = hsvColor.rgb
        thumbDrawable.setCoordinates(x, y)
        thumbDrawable.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> onActionDown(event)
            MotionEvent.ACTION_MOVE -> updateColorOnMotionEvent(event)
            MotionEvent.ACTION_UP -> {
                updateColorOnMotionEvent(event)
            }
        }

        return true
    }

    private fun onActionDown(event: MotionEvent) {
        updateColorOnMotionEvent(event)
    }

    private fun updateColorOnMotionEvent(event: MotionEvent) {
        calculateColor(event)
        fireColorListener()
        invalidate()
    }

    private fun calculateColor(event: MotionEvent) {
        val legX = event.x - wheelCenterX
        val legY = event.y - wheelCenterY
        val hypot = minOf(hypot(legX, legY), wheelRadius.toFloat())
        val hue = (Math.toDegrees(atan2(legY, legX).toDouble()) + 360) % 360
        val saturation = hypot / wheelRadius
        hsvColor.apply {
            this.hue = hue.toFloat()
            this.saturation = saturation
        }
    }

    private fun fireColorListener() {
        colorChangeListener?.invoke(hsvColor.rgb)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val thumbState = thumbDrawable.saveState()
        return ColorWheelState(superState, this, thumbState)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is ColorWheelState) {
            super.onRestoreInstanceState(state.superState)
            readColorWheelState(state)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun readColorWheelState(state: ColorWheelState) {
        thumbDrawable.restoreState(state.thumbState)
        rgb = state.rgb
    }
}
