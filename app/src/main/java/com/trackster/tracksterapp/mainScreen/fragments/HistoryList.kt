package com.trackster.tracksterapp.mainScreen.fragments


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.HistoryRecyclerAdapter
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.History
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONArray

class HistoryList : BaseFragment() {

    private lateinit var historyAdapter: HistoryRecyclerAdapter
    lateinit var apiService: PostApi
    var compositeDisposableContainer = CompositeDisposable()
    private lateinit var description: String
    private lateinit var price: String
    private lateinit var chatId: String
    private var distance: Int? = null
    private var lista : MutableList<History> = mutableListOf()
    private lateinit var recyclerHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyAdapter = HistoryRecyclerAdapter(activity!!)
        val s = PreferenceUtils.getString(context!!)
        val array = JSONArray(s)
        for (i in 0 until array.length()) {
            val row = array.getJSONObject(i)
            description = row.getString("description")
            price = row.getString("price")
            distance = row.getInt("distance")
            chatId = row.getString("id")
            val aa = distance.toString()
            val cc  = History(description,price,aa,chatId)
            lista.add(cc)
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerHistory = view.findViewById(R.id.recyclerHistory)
        initRecyclerView(lista)

    }

    private fun initRecyclerView(lista: MutableList<History>) {
        recyclerHistory.setHasFixedSize(true)
        recyclerHistory.layoutManager = LinearLayoutManager(context)
        recyclerHistory.adapter = historyAdapter
        historyAdapter.setData(lista)
    }

}