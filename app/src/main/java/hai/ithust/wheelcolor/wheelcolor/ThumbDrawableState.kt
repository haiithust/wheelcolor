package hai.ithust.wheelcolor.wheelcolor

import android.os.Parcel
import android.os.Parcelable

internal class ThumbDrawableState private constructor(
    val radius: Int,
    val strokeColor: Int,
) : Parcelable {

    constructor(thumbDrawable: ThumbDrawable) : this(
        thumbDrawable.radius,
        thumbDrawable.strokeColor,
    )

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(radius)
        parcel.writeInt(strokeColor)
    }

    override fun describeContents() = 0

    companion object {

        val EMPTY_STATE = ThumbDrawableState(0, 0)

        @JvmField
        val CREATOR = object : Parcelable.Creator<ThumbDrawableState> {

            override fun createFromParcel(parcel: Parcel) = ThumbDrawableState(parcel)

            override fun newArray(size: Int) = arrayOfNulls<ThumbDrawableState>(size)
        }
    }
}

internal fun Parcel.writeThumbState(state: ThumbDrawableState, flags: Int) {
    this.writeParcelable(state, flags)
}

internal fun Parcel.readThumbState(): ThumbDrawableState {
    return this.readParcelable(ThumbDrawableState::class.java.classLoader)
        ?: ThumbDrawableState.EMPTY_STATE
}
