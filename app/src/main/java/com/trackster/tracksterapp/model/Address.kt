package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")   var city: String,
    @SerializedName("state") var state: String,
    @SerializedName("area") var area: String,
    @SerializedName("street")  var street: String,
    @SerializedName("zipCode") var zipCode: String,
    @SerializedName("plannedTime") var plannedTime: String,
    @SerializedName("location") var location: GeoPoint


)