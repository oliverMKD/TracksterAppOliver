package com.trackster.tracksterapp.model

import java.io.Serializable

data class FirebaseMessage(var message: String?, var contactName: String?, var contactID: Int?,
                           var pushNotificationID: Int?)
    : Serializable