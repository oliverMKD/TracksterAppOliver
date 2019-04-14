package com.trackster.tracksterapp.selectTrailer.fragments

import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment

class SelectColorFragment : BaseFragment() {

    companion object {
        fun newInstance() = SelectColorFragment()
    }
    override fun onBackStackChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_select_color

    override fun getProgressBar(): ProgressBar? = null
}