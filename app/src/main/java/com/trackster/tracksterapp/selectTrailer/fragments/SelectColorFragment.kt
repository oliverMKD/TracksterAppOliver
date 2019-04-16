package com.trackster.tracksterapp.selectTrailer.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import kotlinx.android.synthetic.main.fragment_select_color.*

class SelectColorFragment : BaseFragment(), View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imageView4 -> {
                (activity as SelectTrailerActivity).getSelectedColor()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageView4.setOnClickListener(this)
    }

    companion object {
        fun newInstance() = SelectColorFragment()
    }

    override fun onBackStackChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_select_color

    override fun getProgressBar(): ProgressBar? = null
}