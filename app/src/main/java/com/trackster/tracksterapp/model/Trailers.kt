package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Trailers(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("parentId") val parentId: String
)