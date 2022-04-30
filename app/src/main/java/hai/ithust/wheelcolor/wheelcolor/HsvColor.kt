package hai.ithust.wheelcolor.wheelcolor

import android.graphics.Color
import androidx.annotation.FloatRange

class HsvColor(
    @FloatRange(from = 0.0, to = 360.0)
    var hue: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0)
    var saturation: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0)
    var value: Float = 0f
) {

    var rgb
        get() = Color.HSVToColor(floatArrayOf(hue, saturation, value))
        set(rgb) {
            val hsv = FloatArray(3)
            Color.colorToHSV(rgb, hsv)
            hue = hsv[0]
            saturation = hsv[1]
            value = hsv[2]
        }
}
