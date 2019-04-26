package com.trackster.tracksterapp.model

import java.io.File
import java.io.Serializable


data class AdditionalData(var id: Int = 0, var observerId: Int = 0, var uri: String = "", var imageName: String = "",
                          var uploadFile: File? = null, var time: String = "",
                          var isSending: Boolean = false, var errorSending: Boolean = false) : Serializable