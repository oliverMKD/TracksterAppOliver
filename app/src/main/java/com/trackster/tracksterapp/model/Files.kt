package com.trackster.tracksterapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Files (
    @SerializedName("id") val id: String,
    @SerializedName("filename") var filename: String?
    ) : Parcelable.Creator<Files?> {
    override fun newArray(p0: Int): Array<Files?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createFromParcel(p0: Parcel?): Files? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}