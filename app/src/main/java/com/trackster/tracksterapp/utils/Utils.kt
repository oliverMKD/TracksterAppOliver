package com.trackster.tracksterapp.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.BaseResponse
import com.trackster.tracksterapp.network.connectivity.NoConnectivityException
import retrofit2.HttpException
import java.net.SocketTimeoutException

object Utils {

    fun hideSoftKeyboard(activity: Activity?) {
        if (activity == null)
            return

        val viewInFocus = activity.currentFocus
        if (viewInFocus != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(viewInFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
    fun handleApiError(error: Throwable?) {
        when (error) {
            is HttpException -> {
                try {
                    val response = Gson().fromJson(error.response().errorBody()?.string(), BaseResponse::class.java)
                    val errorMessage = response?.errorMessages?.get(0)
                    if (TextUtils.isEmpty(errorMessage)) {
                        DialogUtils.showGeneralErrorMessage(Activity())
                    } else {
                        DialogUtils.showErrorMessage(Activity(), errorMessage)
                    }
                } catch (exception: JsonSyntaxException) {
                    DialogUtils.showGeneralErrorMessage(Activity())
                }
            }
            is NoConnectivityException -> {
//                DialogUtils.showErrorMessage(Activity(), getString(R.string.general_error_no_internet))
            }
            is SocketTimeoutException -> {
                DialogUtils.showGeneralErrorMessage(Activity())
            }
            else -> {
                DialogUtils.showGeneralErrorMessage(Activity())
            }
        }
    }
}