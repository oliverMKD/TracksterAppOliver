package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.SelectTruckAdapter
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.mainScreen.adapter_Double.SelectTruckAdapterDouble
import com.trackster.tracksterapp.model.Trucks
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import com.trackster.tracksterapp.selectTrailer.fragments.SelectTruckFragment
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_select_truck.*

class SelectTruckFragmentDouble  : BaseFragment(), View.OnClickListener {


    private lateinit var selectTruckAdapter: SelectTruckAdapterDouble
    lateinit var apiService: PostApi
    private var trucksList: MutableList<Trucks> = mutableListOf()

    var compositeDisposableContainer = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectTruckAdapter = SelectTruckAdapterDouble(activity!!)

        getTrucks()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        select_manufactor.setOnClickListener(this)
    }

    companion object {
        fun newInstance() = SelectTruckFragmentDouble()
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
            },
                {
                    Log.d("getTrucks",""+it.localizedMessage)
                })
        )

    }
    override fun onDestroy() {
        compositeDisposableContainer.clear()
        super.onDestroy()
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.select_manufactor -> (activity as MainScreenActivity).openProfileSettingsFragment()
        }

    }
}