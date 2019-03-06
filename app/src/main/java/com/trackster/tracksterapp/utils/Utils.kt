package com.trackster.tracksterapp.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

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
}