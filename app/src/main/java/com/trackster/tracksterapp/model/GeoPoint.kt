package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class GeoPoint (@SerializedName("lat") val lat : Double,
                     @SerializedName("long") val long : Double)