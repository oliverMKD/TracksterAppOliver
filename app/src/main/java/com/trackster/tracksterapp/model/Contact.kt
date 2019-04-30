package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Contact (var id: Int, var nickname: String, var avatar: String,
                    @SerializedName("last_message") var lastMessage: String,
                    @SerializedName("last_message_date") var lastMessageDate: String,
                    @SerializedName("unread_count") var unreadCount: Int) : Serializable