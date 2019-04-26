package com.trackster.tracksterapp.network

import android.content.Context
import com.trackster.tracksterapp.utils.BASE_URL
import com.trackster.tracksterapp.utils.Config
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AmazonApiService {

    @GET("config.json")
    fun getConfig(): Observable<Config>

    companion object Factory {
        fun create(context: Context): AmazonApiService {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build()

            return retrofit.create(AmazonApiService::class.java)
        }
    }
}