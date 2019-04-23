package com.trackster.tracksterapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data public class Broker (

    @SerializedName("id")
    @Expose
    private  val id : String,
    @SerializedName("email")
    @Expose
    private  val email:String,
    @SerializedName("lastName")
    @Expose
    private val lastName: String,
    @SerializedName("firstName")
    @Expose
    private val firstName: String,
    @SerializedName("phone")
    @Expose
    private val phone: String,
    @SerializedName("image")
    @Expose
    private val image: String,
    @SerializedName("companyId")
    @Expose
    private val companyId : String,
    @SerializedName("company")
    @Expose
    private val company: Company,
    @SerializedName("userType")
    @Expose
    private val userType: Int,
    @SerializedName("trailerType")
    @Expose
    private val trailerType: Int,
    @SerializedName("password")
    @Expose
    private val password : String,
    @SerializedName("isPhoneConfirmed")
    @Expose
    private val isPhoneConfirmed: Boolean,
    @SerializedName("awayFromKeyboard")
    @Expose
    private val awayFromKeyboard: Boolean,
    @SerializedName("sendSmsNotifications")
    @Expose
    private val sendSmsNotifications: Boolean,
    @SerializedName("sendEmailNotifications")
    @Expose
    private val sendEmailNotifications: Boolean

)