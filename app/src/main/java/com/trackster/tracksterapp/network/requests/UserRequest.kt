package com.trackster.tracksterapp.network.requests

import com.google.gson.annotations.SerializedName

data class UserRequest (@SerializedName("firstName")var name: String, var id : String, var email : String)