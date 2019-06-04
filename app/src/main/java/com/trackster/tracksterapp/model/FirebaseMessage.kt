package com.trackster.tracksterapp.model

import java.io.Serializable

data class FirebaseMessage(var id: String,

                           var content: String,
                           var createBy : String,
                           var senderId : String,
                           var createTime : String)
    : Serializable