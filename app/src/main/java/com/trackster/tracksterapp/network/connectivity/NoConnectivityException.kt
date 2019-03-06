package com.trackster.tracksterapp.network.connectivity
import java.io.IOException

class NoConnectivityException(private val errorMessage: String): IOException() {

    override fun getLocalizedMessage(): String = errorMessage
}