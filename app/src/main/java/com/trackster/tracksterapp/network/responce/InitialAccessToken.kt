package com.trackster.tracksterapp.network.responce

import com.google.gson.annotations.SerializedName

data class InitialAccessToken (@SerializedName("token") var accessToken: String)