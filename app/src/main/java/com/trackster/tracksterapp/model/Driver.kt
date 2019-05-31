package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Driver (@SerializedName("email") val email: String,
                   @SerializedName("firstName") val firstName: String,
                   @SerializedName("lastName") val lastName: String,
                   @SerializedName("phone") val phone: String)