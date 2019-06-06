package com.trackster.tracksterapp.network

import android.content.Context
import com.trackster.tracksterapp.model.*
import com.trackster.tracksterapp.network.connectivity.ConnectivityInterceptor
import com.trackster.tracksterapp.network.connectivity.HeaderInterceptor
import com.trackster.tracksterapp.network.requests.*
import com.trackster.tracksterapp.network.responce.ChatResponse
import com.trackster.tracksterapp.network.responce.ChatResponseId
import com.trackster.tracksterapp.utils.BASE_URL
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type


interface PostApi {
    /**
     * Get the list of the pots from the API
     */
    @Headers("Content-Type: application/json")
    @POST("api/auth/login/")
    fun login(@Body loginRequest: LoginRequest): Observable<Response<User>>

    @Headers("Content-Type: application/json")
    @POST("api/auth/facebook/")
    fun loginFB(@Body fbLoginRequest: FbLoginRequest): Observable<Response<User>>

    @POST("api/auth/phone/")
    fun loginWithPhone(@Body loginRequestWithPhone: LoginRequestWithPhone): Observable<Response<User>>

    @POST("api/auth/phone/")
    fun validatePhone(@Body validatePhoneRequest: ValidatePhoneRequest): Observable<Response<User>>

    @GET("api/admin/trailers/default")
    fun getDefaultTrailers(@Header("x-auth-token") authorization: String): Observable<ArrayList<Trailers>>

    @GET("api/admin/trailers/others")
    fun getOtherTrailers(@Header("x-auth-token") authorization: String): Observable<ArrayList<Trailers>>

    @GET("api/admin/truck-models")
    fun getTrucks(@Header("x-auth-token") authorization: String): Observable<ArrayList<Trucks>>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("api/weigh-stations/circle")
    fun getWeighStations(
        @Header("x-auth-token") authorization: String,
        @Query("center") centar: String,
        @Query("radius") radius: Int
    ): Single<ArrayList<WeighStation>>

    @GET("api/chats")
    fun getChats(@Header("x-auth-token") authorization: String): Observable<ArrayList<ChatResponse>>


    @GET("api/chats/{chatId}")
    fun getChatById(
        @Header("x-auth-token") authorization: String,
        @Path("chatId") chatId: String
    ): Single<ChatResponseId>

    @GET("api/users/me")
    fun getInfoUser(@Header("x-auth-token") authorization: String): Observable<Response<User>>

    @Headers("Content-Type: application/json")
    @POST("api/users")
    fun updateUser(@Header("x-auth-token") authorization: String, @Body fbLoginRequest: UserRequest): Observable<Response<User>>

    @GET("api/chats")
    fun getDetails(@Header("x-auth-token") authorization: String): Observable<ArrayList<ChatResponse>>

    @GET("api/chats")
    fun getHistory(@Header("x-auth-token") authorization: String): Observable<ArrayList<ChatResponse>>

    @POST("api/chats/{chatId}/push/message")
    fun postMessage(
        @Header("x-auth-token") authorization: String,
        @Path("chatId") chatId: String, @Body message: Message
    ): Single<Message>

    @GET("api/admin/truck-colors")
    fun getColors(@Header("x-auth-token") authorization: String): Observable<ArrayList<Colors>>

    @Multipart
    @POST("api/chats/upload/{chatId}")
    fun postAudio(
        @Header("x-auth-token") authorization: String,
        @Path("chatId") chatId: String, @Part image: MultipartBody.Part
    ): Single<Message>


    @GET("api/chats/{chatId}/files/{filename}")
    fun getFiles(
        @Header("x-auth-token") authorization: String,
        @Path("chatId") chatId: String, @Path("filename") filename: String
    ): Observable<Response<ResponseBody>>

    @GET("api/chats/{chatId}/files/{filename}")
    fun getFileById(
        @Header("x-auth-token") authorization: String,
        @Path("chatId") chatId: String, @Path("filename") filename: String
    ): Observable<Response<ResponseBody>>

    @POST("api/notifications/register-device")
    fun postFirebaseToken(@Header("x-auth-token") authorization: String, @Body deviceToken: String): Observable<Response<ResponseBody>>

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



