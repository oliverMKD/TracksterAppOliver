package com.trackster.tracksterapp.network

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.model.User
import com.trackster.tracksterapp.model.WeighStation
import com.trackster.tracksterapp.network.connectivity.ConnectivityInterceptor
import com.trackster.tracksterapp.network.requests.FbLoginRequest
import com.trackster.tracksterapp.network.requests.LoginRequest
import com.trackster.tracksterapp.network.responce.ChatResponse
import com.trackster.tracksterapp.network.responce.InitialAccessToken
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.trackster.tracksterapp.utils.BASE_URL
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.http.*
import java.lang.reflect.Type


interface PostApi {
    /**
     * Get the list of the pots from the API
     */
    @Headers("Content-Type: application/json")
    @POST("auth/login/")
    fun login(@Body loginRequest: LoginRequest): Observable<Response<User>>

    @Headers("Content-Type: application/json")
    @POST("auth/facebook/")
    fun loginFB(@Body fbLoginRequest: FbLoginRequest): Observable<Response<User>>


    @GET("weigh-stations/circle")
    fun getWeighStations(@Header("x-auth-token") authorization : String,@Header("Content-Type") format : String, @Query ("centar") centar : LatLng,
                         @Query ("radius") radius : Int ): Observable<WeighStation>

    @GET("/admin/trailers")
    fun getTrailers(@Header("x-auth-token") authorization : String): Observable<ArrayList<Trailers>>

    @GET("/chats")
    fun getChats(@Header("x-auth-token") authorization : String): Observable<ArrayList<ChatResponse>>

    @GET("/chats/{chatId}")
    fun getChatById(@Header("x-auth-token") authorization : String, @Path("chatId") chatId : String): Observable<ChatResponse>


    companion object Factory {
        fun create(context: Context): PostApi {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(ConnectivityInterceptor(context))
                .build()

            val nullOnEmptyConverterFactory = object : Converter.Factory() {
                fun converterFactory() = this
                override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) =
                    object :
                        Converter<ResponseBody, Any?> {
                        val nextResponseBodyConverter =
                            retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

                        override fun convert(value: ResponseBody) =
                            if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
                    }
            }

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            return retrofit.create(PostApi::class.java)
        }
    }
}