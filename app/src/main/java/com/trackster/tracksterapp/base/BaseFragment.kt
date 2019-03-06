package com.trackster.tracksterapp.base

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import com.trackster.tracksterapp.utils.DialogUtils

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

//    override fun showError() {
//        DialogUtils.showGeneralErrorMessage(activity)
//    }
//
//    override fun showError(message: String) {
//        DialogUtils.showErrorMessage(activity, message)
//    }
//
//    override fun showSuccessMessage(message: String) {
//        DialogUtils.showSuccessMessage(activity, message)
//    }

    fun EditText.loadFuturaMediumFont(){
        this.typeface = Typeface.createFromAsset(context?.assets,"fonts/futura_bt_medium.ttf")
    }

    fun EditText.loadFuturaBoldFont(){
        this.typeface = Typeface.createFromAsset(context?.assets,"fonts/futura_bt_bold.ttf")
    }



    abstract fun getLayoutId(): Int

    abstract fun getProgressBar(): ProgressBar?

//    override fun setLoadingIndicator(active: Boolean) {
//        if (active) {
//            getProgressBar()?.visibility = View.VISIBLE
//        } else {
//            getProgressBar()?.visibility = View.GONE
//        }
//    }

    fun EditText.isNotNullEmptyOrWhitespace(): Boolean {
        return !this.text.isNullOrEmpty() && this.text.isNotBlank()
    }


}

