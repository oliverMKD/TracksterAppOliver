package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email") val email: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("image") val image: String,
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("token") val token: String,
    @SerializedName("password") val password: String,
    @SerializedName("userType") val userType: Int,
    @SerializedName("companyId") val companyId: String,
    @SerializedName("trailerType") val trailerType: Int,
    @SerializedName("isPhoneConfirmed") val isPhoneConfirmed: Boolean,
    @SerializedName("awayFromKeyboard") val awayFromKeyboard: Boolean,
    @SerializedName("sendSmsNotifications") val sendSmsNotifications: Boolean,
    @SerializedName("sendEmailNotifications") val sendEmailNotifications: Boolean,
    @SerializedName("company") val company: Company
)

