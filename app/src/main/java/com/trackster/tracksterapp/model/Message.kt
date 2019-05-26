package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Message(

    var id: String,

    var content: String,
    var createBy : String,
    var senderId : String,
    var createTime : String,
    var file: Files?
//var isMine : Boolean

) : Serializable