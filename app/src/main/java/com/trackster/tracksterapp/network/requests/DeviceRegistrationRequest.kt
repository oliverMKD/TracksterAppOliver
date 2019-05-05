package com.trackster.tracksterapp.network.requests

import com.google.gson.annotations.SerializedName

data class DeviceRegistrationRequest (@SerializedName("deviceToken") var deviceToken : String)