package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Shipment (@SerializedName("carrierId") val carrierId : String,
                     @SerializedName("driverId") val driverId : String,
                     @SerializedName("description") val description : String,
                     @SerializedName("price") val price : Int)