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

    fun EditText.loadFuturaMediumFont(){
        this.typeface = Typeface.createFromAsset(context?.assets,"fonts/futura_bt_medium.ttf")
    }

    fun EditText.loadFuturaBoldFont(){
        this.typeface = Typeface.createFromAsset(context?.assets,"fonts/futura_bt_bold.ttf")
    }



    abstract fun getLayoutId(): Int

    abstract fun getProgressBar(): ProgressBar?

    fun EditText.isNotNullEmptyOrWhitespace(): Boolean {
        return !this.text.isNullOrEmpty() && this.text.isNotBlank()
    }

    abstract fun onBackStackChanged()
}

