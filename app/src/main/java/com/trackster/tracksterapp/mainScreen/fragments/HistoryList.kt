package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.HistoryRecyclerAdapter

import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.ChatResponse

import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.history_fragment.*

class HistoryList : BaseFragment() {

    private lateinit var historyAdapter: HistoryRecyclerAdapter
    lateinit var apiService: PostApi
    private var historyList: MutableList<ChatResponse> = mutableListOf()
    var fragmentPosition: Int = 0

    //    private var disposable: CompositeDisposable? = null
    var compositeDisposableContainer = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyAdapter = HistoryRecyclerAdapter(activity!!)
        getHistory()
    }




    override fun onBackStackChanged() {



    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.history_fragment

    companion object {
        fun newInstance() = HistoryList()
    }

    override fun onDestroy() {
        compositeDisposableContainer.clear()
        (activity as MainScreenActivity).show()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    private fun initRecyclerView(list: ArrayList<ChatResponse>) {
        recyclerHistory.setHasFixedSize(true)
        recyclerHistory.layoutManager = LinearLayoutManager(context)
        recyclerHistory.adapter = historyAdapter
        historyAdapter.setData(list)
    }




    private fun getHistory() {

        apiService = PostApi.create(context!!)
        compositeDisposableContainer.add(apiService.getHistory(
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



}