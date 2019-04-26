package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Message(var id: Int,
                   var sender: Int,
                   var recipient: Int,
                   @SerializedName("send_time") var sendTime: String,
                   var content: String,
                   var status: String,
                   @SerializedName("is_mine") var isMine: Boolean,
                   var read: Boolean,
                   @SerializedName("image_url") var imageUrl: String,
                   @SerializedName("video_url") var videoUrl: String,
                   @SerializedName("is_portrait") var isPortrait: Boolean,
                   var additionalData: AdditionalData) : Serializable