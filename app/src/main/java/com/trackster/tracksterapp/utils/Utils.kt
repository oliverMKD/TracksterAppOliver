package com.trackster.tracksterapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import com.amazonaws.ClientConfiguration
import com.trackster.tracksterapp.model.Message

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
    fun getScreenWidth(activity: Activity?): Int {
        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getRealSize(size)
        return size.x
    }
    fun getColor(context: Context, color: Int): Int {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.getColor(context, color)
        }

        return ContextCompat.getColor(context, color)
    }

    fun hasMessageMedia(message: Message) =
        !TextUtils.isEmpty(message.imageUrl) || !TextUtils.isEmpty(message.videoUrl)

    fun getAWSConfigurationClient(): ClientConfiguration {
        val cc = ClientConfiguration()
        cc.connectionTimeout = 300000 // 300 seconds

        return cc
    }
}