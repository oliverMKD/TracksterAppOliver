package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.model.User
import com.trackster.tracksterapp.network.PostApi
import io.reactivex.disposables.CompositeDisposable

class HistoryList : BaseFragment() {

//    lateinit var apiService: PostApi
//    private var userInfo: MutableList<User> = mutableListOf()
//    var fragmentPosition: Int = 0
//    var compositeDisposableContainer = CompositeDisposable()





    override fun onBackStackChanged() {

    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.history_fragment

    companion object {
        fun newInstance() = HistoryList()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}