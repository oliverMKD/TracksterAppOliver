package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("emailDomain") val emailDomain: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("physicalAddress") val physicalAddress: String,
    @SerializedName("mailingAddress") val mailingAddress: String,
    @SerializedName("companyType") val companyType: Int,
    @SerializedName("usDotNumber") val usDotNumber: String,
    @SerializedName("dunsNumber") val dunsNumber: String,
    @SerializedName("stateCarrierId") val stateCarrierId: String
)