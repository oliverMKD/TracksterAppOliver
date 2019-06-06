package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.mainScreen.adapter_Double.SelectColorAdapterDouble
import com.trackster.tracksterapp.model.Colors
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_select_color.*

class SelectColorFragmentDouble : BaseFragment(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.select_manufactor_color -> (activity as MainScreenActivity).openProfileSettingsFragmentColor()
        }
    }
    private lateinit var selectColorAdapter: SelectColorAdapterDouble
    lateinit var apiService: PostApi

    var compositeDisposableContainer = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectColorAdapter = SelectColorAdapterDouble(activity!!)
        getTrucks()
    }

    companion object {
        fun newInstance() = SelectColorFragmentDouble()
    }

    override fun onBackStackChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_select_color

    override fun getProgressBar(): ProgressBar? = null

    private fun initRecyclerView(list: ArrayList<Colors>) {
        recycler_color.setHasFixedSize(true)
        recycler_color.layoutManager = GridLayoutManager(context, 3)
        recycler_color.adapter = selectColorAdapter
        selectColorAdapter.setData(list)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        select_manufactor_color.setOnClickListener(this)
    }

    private fun getTrucks() {

        apiService = PostApi.create(context!!)
        compositeDisposableContainer.add(
            apiService.getColors(
                PreferenceUtils.getAuthorizationToken(context!!))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    initRecyclerView(it)
                }, {

                })
        )
    }

    override fun onDestroy() {
        compositeDisposableContainer.dispose()
        super.onDestroy()
    }
}