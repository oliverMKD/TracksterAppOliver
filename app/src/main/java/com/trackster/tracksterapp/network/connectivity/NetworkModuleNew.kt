package com.trackster.tracksterapp.network.connectivity

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trackster.tracksterapp.network.OkHttpClientModule
import com.trackster.tracksterapp.network.PostApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [(OkHttpClientModule::class)])
class NetworkModuleNew {

    companion object {
        //dev server
        const val BASE_URL = "http://34.73.255.52:3000/"
        //prod server
        //  const val BASE_URL = "https://clubcity.ch/adminaccess450/"
    }

    @Provides
    @Singleton
    fun clubCityApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }
}