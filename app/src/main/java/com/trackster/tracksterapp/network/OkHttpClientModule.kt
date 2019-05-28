package com.trackster.tracksterapp.network

import android.content.Context
import android.util.Log
import com.trackster.tracksterapp.base.ApplicationModule
import com.trackster.tracksterapp.network.connectivity.ConnectivityInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [(ApplicationModule::class)])
class OkHttpClientModule {

    companion object {
        const val HTTP_CACHE = "HttpCache"
        const val CONNECT_TIMEOUT = 30L
        const val READ_TIMEOUT = 30L
    }

    @Provides
    @Singleton
    fun okHttpClient( httpLoggingInterceptor: HttpLoggingInterceptor, context: Context): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(ConnectivityInterceptor(context))
            .addNetworkInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)


        return builder.build()
    }

//    @Provides
//    @Singleton
//    fun cache(cacheFile: File): Cache {
//        return Cache(cacheFile, 10 * 1000 * 1000)
//    }
//
//    @Provides
//    @Singleton
//    fun cacheFile(context: Context): File {
//        val file = File(context.cacheDir, HTTP_CACHE)
//        file.mkdirs()
//        return file
//    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.i(" http : ", it)
        })

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpLoggingInterceptor
    }

}

