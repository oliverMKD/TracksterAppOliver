package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Files (
    @SerializedName("id") val id: String,
    @SerializedName("filename") var filename: String



    )