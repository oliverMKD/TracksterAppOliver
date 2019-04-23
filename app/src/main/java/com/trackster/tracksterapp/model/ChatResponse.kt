package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("distance") val distance: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("broker") val broker: User,
    @SerializedName("pickupAddresses") val pickupAddresses: Address,
    @SerializedName("destinationAddresses") val destinationAddresses: Address


)

