package com.trackster.tracksterapp.mainScreen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity

import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_load_screen.*

class CurrentLoad : BaseFragment() {


    lateinit var apiService: PostApi
    var compositeDisposableContainer = CompositeDisposable()

    override fun onBackStackChanged() {

    }
    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.fragment_load_screen

    companion object {
        fun newInstance() = CurrentLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLoad()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposableContainer.clear()
        (activity as MainScreenActivity).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("LogNotTimber")
    private fun getLoad() {
        apiService = PostApi.create(context!!)
        compositeDisposableContainer.add(
            apiService.getHistory(
                PreferenceUtils.getAuthorizationToken(context!!))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("getLoad", "" + it[0].description)
                    name_load.text = ( it[0].description)
                    Glide.with(activity!!)
                        .load(it[0].broker.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(slika)

                }, {
                    Log.d("getLoad", "" + it.localizedMessage)
                })
        )

    }

}