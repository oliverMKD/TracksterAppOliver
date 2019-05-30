package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Legs (@SerializedName("start_location") val start_location : StartLocation,
                 @SerializedName("end_location") val end_location : EndLocation)