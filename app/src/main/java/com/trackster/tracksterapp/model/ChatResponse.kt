package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName
import java.nio.file.Files

data class ChatResponse(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("distance") val distance: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("broker") val broker: User,
    @SerializedName("Files") val files: com.trackster.tracksterapp.model.Files,
    @SerializedName("pickupAddresses") val pickupAddresses: Address,
    @SerializedName("destinationAddresses") val destinationAddresses: Address


)

