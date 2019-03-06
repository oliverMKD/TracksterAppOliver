package com.trackster.tracksterapp.network

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("error_descriptions") var errorMessages: List<String>)