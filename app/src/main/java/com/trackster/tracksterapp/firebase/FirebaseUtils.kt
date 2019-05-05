package com.trackster.tracksterapp.firebase

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

object FirebaseUtils {

    private const val TAG = "TVChat"

    /**
     * Get updated FirebaseInstanceID token.
     */
    fun refreshFirebaseToken(context: Context) {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

    }

    /**
     * Persist Firebase token to third-party server.
     */
    fun sendFirebaseTokenToServer(context: Context, token: String) {
//        if (TextUtils.isEmpty(token)) return
//        CompositeDisposable().add(PostApi.create(context)
//                .register(PreferenceUtils.getAuthorizationToken(context), RegisterFirebaseRequest(token))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe({ _ ->
//                    Log.d(TAG, "Token successfully sent! $token")
//                }, { error ->
//                    Log.e(TAG, "Token NOT sent! ${error.message}")
//                }))
    }

    /**
     * Deletes Firebase token, so that it will be refreshed when a new user is logged in.
     */
    fun deleteFirebaseInstanceId(context: Context) {
//        try {
//            FirebaseInstanceId.getInstance().deleteInstanceId()
//            FirebaseInstanceId.getInstance().token
//            PreferenceUtils.saveFirebaseToken(context, "")
//            Log.d(TAG, "Firebase instance ID deleted!")
//        } catch (exception: IOException) {
//            Log.d(TAG, "Could not delete the Firebase instance ID! ${exception.message}")
//        }
    }

    fun putPushNotificationId(context: Context, pushNotificationId: Int?) {
//        if (pushNotificationId != null) {
//            CompositeDisposable().add(PostApi.create(context)
//                    .postPushNotificationId(pushNotificationId,
//                            PreferenceUtils.getAuthorizationToken(context))
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe({ _ ->
//                        Log.d(TAG, "Firebase push id successfully sent!")
//                    }, { _ ->
//                        Log.d(TAG, "Firebase push id NOT sent!")
//                    }))
//        }
    }

}