package com.trackster.tracksterapp.network

import android.content.Context
import android.support.v4.media.AudioAttributesCompat
import com.google.android.gms.maps.model.LatLng
import com.trackster.tracksterapp.model.*
import com.trackster.tracksterapp.network.connectivity.ConnectivityInterceptor
import com.trackster.tracksterapp.network.connectivity.HeaderInterceptor
import com.trackster.tracksterapp.network.requests.*

import com.trackster.tracksterapp.network.responce.ChatResponse
import com.trackster.tracksterapp.network.responce.InitialAccessToken
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.trackster.tracksterapp.utils.BASE_URL
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.http.*
import java.io.IOException
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

    @POST("/auth/phone/")
    fun loginWithPhone(@Body loginRequestWithPhone: LoginRequestWithPhone): Observable<Response<User>>

    @POST("/auth/phone/")
    fun validatePhone(@Body validatePhoneRequest: ValidatePhoneRequest): Observable<Response<User>>

    @GET("/admin/trailers/default")
    fun getDefaultTrailers(@Header("x-auth-token") authorization: String): Observable<ArrayList<Trailers>>

    @GET("/admin/trailers/others")
    fun getOtherTrailers(@Header("x-auth-token") authorization: String): Observable<ArrayList<Trailers>>

    @GET("/admin/truck-models")
    fun getTrucks(@Header("x-auth-token") authorization: String): Observable<ArrayList<Trucks>>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("weigh-stations/circle")
    fun getWeighStations(
        @Header("x-auth-token") authorization: String,
        @Query ("center")  centar : String,
        @Query("radius") radius: Int
    ): Single<ArrayList<WeighStation>>

    @GET("/chats")
    fun getChats(@Header("x-auth-token") authorization: String): Observable<ArrayList<ChatResponse>>


    @GET("/chats/{chatId}")
    fun getChatById(@Header("x-auth-token") authorization: String,
                    @Path("chatId") chatId: String) : Single<ChatResponse>

    @GET("/users/me")
    fun getInfoUser(@Header("x-auth-token") authorization : String): Observable<Response<User>>

    @Headers("Content-Type: application/json")
    @POST("/users")
    fun updateUser(@Header("x-auth-token") authorization : String,@Body fbLoginRequest: UserRequest): Observable<Response<User>>

    @GET("/chats")
    fun getDetails(@Header("x-auth-token") authorization : String): Observable<ArrayList<com.trackster.tracksterapp.model.ChatResponse>>

    @GET("/chats")
    fun getHistory(@Header("x-auth-token") authorization: String): Observable<ArrayList<com.trackster.tracksterapp.model.ChatResponse>>

    @POST("/chats/{chatId}/push/message")
    fun postMessage(@Header("x-auth-token") authorization: String,
                    @Path("chatId") chatId: String, @Body message: Message) : Single<Message>
    @GET("/admin/truck-colors")
    fun getColors(@Header("x-auth-token") authorization: String): Observable<ArrayList<Colors>>

    @Multipart
    @POST("/chats/upload/{chatId}")
    fun postAudio(@Header("x-auth-token") authorization: String,
                    @Path("chatId") chatId: String, @Part("file\"; filename=\"recorder.aac\" ") file: RequestBody   ) : Single<Files>

    companion object Factory {
        fun create(context: Context): PostApi {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(ConnectivityInterceptor(context))
                .addNetworkInterceptor(HeaderInterceptor())
                .build()
            val head = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor())
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



