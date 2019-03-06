package com.trackster.tracksterapp.network.connectivity
import android.content.Context
import com.trackster.tracksterapp.R
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(ConnectivityUtils.isConnected(context))
            return chain.proceed(chain.request())
        else
            throw NoConnectivityException(context.getString(R.string.general_error_no_internet))
    }
}