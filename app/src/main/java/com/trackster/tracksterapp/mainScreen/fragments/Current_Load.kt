package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment

class Current_Load : BaseFragment() {

    override fun onBackStackChanged() {

    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.fragment_load_screen

    companion object {
        fun newInstance() = Current_Load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}