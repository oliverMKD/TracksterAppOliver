package com.trackster.tracksterapp.selectTrailer.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.GridLayout
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.SelectTruckAdapter
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.model.Trucks
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_select_trailer.*
import kotlinx.android.synthetic.main.fragment_select_truck.*

class SelectTruckFragment : BaseFragment() {

    private lateinit var selectTruckAdapter: SelectTruckAdapter
    lateinit var apiService: PostApi
    private var trucksList: MutableList<Trucks> = mutableListOf()
    var fragmentPosition: Int = 0

//    private var disposable: CompositeDisposable? = null
    var compositeDisposableContainer = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectTruckAdapter = SelectTruckAdapter(activity!!)
        getTrucks()

    }

    companion object {
        fun newInstance() = SelectTruckFragment()
    }

    override fun onBackStackChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_select_truck

    override fun getProgressBar(): ProgressBar? = null

    private fun initRecyclerView(list: ArrayList<Trucks>) {
        recycler_manufactor.setHasFixedSize(true)
        recycler_manufactor.layoutManager = GridLayoutManager(context, 3)
        recycler_manufactor.adapter = selectTruckAdapter
        selectTruckAdapter.setData(list)
    }


    private fun getTrucks() {

        apiService = PostApi.create(context!!)
        compositeDisposableContainer.add(apiService.getTrucks(
            PreferenceUtils.getAuthorizationToken(context!!)
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                initRecyclerView(it)
// Log.d("station", " "+ it[0].location)
            }, {
                // showProgress(false)
//                Utils.handleApiError(it)
            })
        )

    }
    override fun onDestroy() {
        compositeDisposableContainer.clear()
        super.onDestroy()
    }
}