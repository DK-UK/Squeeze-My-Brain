package com.example.squeezemybrain.data.network.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Questions(
    val response_code: Int,
    val results: List<Result>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        TODO("results")
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Questions> {
        override fun createFromParcel(parcel: Parcel): Questions {
            return Questions(parcel)
        }

        override fun newArray(size: Int): Array<Questions?> {
            return arrayOfNulls(size)
        }
    }
}