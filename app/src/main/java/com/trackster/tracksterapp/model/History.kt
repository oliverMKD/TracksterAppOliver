package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class History( @SerializedName("description") val description: String,
@SerializedName("price") val price: String,
@SerializedName("distance") val distance: String,
                    @SerializedName("id") val id: String)