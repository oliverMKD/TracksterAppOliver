package com.trackster.tracksterapp.model

import java.io.Serializable

data class FirebaseMessage(var senderId: String,
                           var content: String) : Serializable