package hai.ithust.wheelcolor.wheelcolor

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import hai.ithust.wheelcolor.R
import hai.ithust.wheelcolor.databinding.LayoutColorSegmentBinding

class SegmentColorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val binding = LayoutColorSegmentBinding.inflate(LayoutInflater.from(context), this)
    var contentColor: Int = 0

    init {
        parseAttributes(context, attrs)
        setBackgroundResource(R.drawable.selector_segment)
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SegmentColorView, 0, 0)
        contentColor = array.getColor(R.styleable.SegmentColorView_sg_color, 0)
        binding.viewCircle.backgroundTintList = ColorStateList.valueOf(contentColor)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthMeasureSpec < heightMeasureSpec)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        else
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
    }
}