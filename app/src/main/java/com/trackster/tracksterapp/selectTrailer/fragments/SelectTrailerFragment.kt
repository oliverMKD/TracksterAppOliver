package com.trackster.tracksterapp.selectTrailer.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.OtherTrailersRecyclerAdapter
import com.trackster.tracksterapp.adapters.TrailersRecyclerAdapter
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SelectTrailerFragment : BaseFragment(),View.OnClickListener {

    companion object {
        fun newInstance() = SelectTrailerFragment()
    }

    lateinit var listView: RecyclerView
    lateinit var recyclerViewOthers : RecyclerView
    private lateinit var adapter: TrailersRecyclerAdapter
    private lateinit var adapterOthers : OtherTrailersRecyclerAdapter
    lateinit var apiService: PostApi
    private lateinit var mOthers : TextView
    private val SPLASH_DELAY: Long = 1500 //1.5 seconds
    private var mDelayHandler: Handler? = null
    private var messagesList: MutableList<Trailers> = mutableListOf()
    var fragmentPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listView.findViewById<RecyclerView>(R.id.recyclerViewTrailer)
        recyclerViewOthers.findViewById<RecyclerView>(R.id.recyclerViewTrailerOthers)
        adapter = TrailersRecyclerAdapter(activity!!)
        adapterOthers = OtherTrailersRecyclerAdapter(activity!!)
        mOthers.findViewById<TextView>(R.id.text_other)
        mOthers.setOnClickListener(this)
        initRecyclerView()
        getTrailers()
        getOtherTrailers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onBackStackChanged() {
        if (fragmentPosition == requireFragmentManager().backStackEntryCount) {
            this.onResume()
        }
    }

    private fun initRecyclerView() {
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(context)
        messagesList.add(0, (Trailers("prvi", "sofer1", "golem paket")))
        messagesList.add(1, (Trailers("vtori", "sofer2", "golem paket")))
        messagesList.add(2, (Trailers("treti", "sofer3", "golem paket")))
        messagesList.add(3, (Trailers("cetvrti", "sofer4", "golem paket")))
        messagesList.add(4, (Trailers("petti", "sofer5", "golem paket")))
        listView.adapter = adapter
        adapter.setData(messagesList)
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.text_other-> recyclerViewOthers.visibility = View.VISIBLE
        }
    }
    private fun initRecyclerView(list : ArrayList<Trailers>) {
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(context)
        listView.adapter = adapter
        adapter.setData(list)
    }
    private fun initRecyclerViewOthers(list : ArrayList<Trailers>) {
        recyclerViewOthers.setHasFixedSize(true)
        recyclerViewOthers.layoutManager = LinearLayoutManager(context)
        recyclerViewOthers.adapter = adapterOthers
        adapterOthers.setData(list)
    }

    private fun getTrailers(){
        apiService = PostApi.create(context!!)
        CompositeDisposable().add(apiService.getDefaultTrailers(
            PreferenceUtils.getAuthorizationToken(context!!))
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
    private fun getOtherTrailers(){
        apiService = PostApi.create(context!!)
        CompositeDisposable().add(apiService.getOtherTrailers(
            PreferenceUtils.getAuthorizationToken(context!!))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                recyclerViewOthers.visibility = View.VISIBLE
                initRecyclerViewOthers(it)
// Log.d("station", " "+ it[0].location)
            }, {
                // showProgress(false)
//                Utils.handleApiError(it)
            })
        )
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getLayoutId(): Int = R.layout.fragment_select_trailer

    override fun getProgressBar(): ProgressBar? = null
}