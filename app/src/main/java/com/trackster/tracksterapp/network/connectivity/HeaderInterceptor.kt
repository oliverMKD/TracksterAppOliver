package com.trackster.tracksterapp.network.connectivity

import okhttp3.Interceptor

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }
}