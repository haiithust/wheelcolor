package hai.ithust.wheelcolor.wheelcolor

import android.os.Parcel
import android.os.Parcelable
import android.view.View

internal class ColorWheelState : View.BaseSavedState {

    val thumbState: ThumbDrawableState
    val rgb: Int

    constructor(
        superState: Parcelable?,
        view: ColorWheel,
        thumbState: ThumbDrawableState
    ) : super(superState) {
        this.thumbState = thumbState
        rgb = view.rgb
    }

    constructor(source: Parcel) : super(source) {
        thumbState = source.readThumbState()
        rgb = source.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeThumbState(thumbState, flags)
        out.writeInt(rgb)
    }

    companion object CREATOR : Parcelable.Creator<ColorWheelState> {

        override fun createFromParcel(source: Parcel) = ColorWheelState(source)

        override fun newArray(size: Int) = arrayOfNulls<ColorWheelState>(size)
    }
}
