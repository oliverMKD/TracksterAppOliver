package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class WeighStation (@SerializedName("isOpen") val isOpen : Boolean,
                         @SerializedName("location") val location : GeoPoint )