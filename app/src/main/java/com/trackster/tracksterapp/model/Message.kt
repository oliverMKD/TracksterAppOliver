package com.trackster.tracksterapp.model

import android.annotation.TargetApi
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Message(

    var id: String,

    var content: String,
    var createBy : String,
    var senderId : String,
    var createTime : String,
   final var file: Files?
//var isMine : Boolean

) : Serializable
