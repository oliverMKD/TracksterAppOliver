package com.trackster.tracksterapp.network.responce

import com.google.gson.annotations.SerializedName
import com.trackster.tracksterapp.model.Address
import com.trackster.tracksterapp.model.Files
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.model.User

data class ChatResponse(
    @SerializedName("id") var id: String,
    @SerializedName("description") var description: String,
    @SerializedName("plannedPickupTime") var plannedPickupTime: String,
    @SerializedName("plannedDestinationTime") var plannedDestinationTime: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("price") var price: Int,
    @SerializedName("status") var status: Int,
    @SerializedName("pickupAddresses") var pickupAddress: ArrayList<Address>,
    @SerializedName("destinationAddresses") var destinationAddress: ArrayList<Address>,
    @SerializedName("broker") var broker: User,
    @SerializedName("carrier") var carrier: User,
    @SerializedName("driver") var driver: User,
    @SerializedName("messages") var message: ArrayList<Message>
)
