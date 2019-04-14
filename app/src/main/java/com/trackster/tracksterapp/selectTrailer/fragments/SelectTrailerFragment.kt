package com.trackster.tracksterapp.selectTrailer.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import kotlinx.android.synthetic.main.fragment_select_trailer.*

class SelectTrailerFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance() = SelectTrailerFragment()
    }

    private lateinit var adapter: TrailersRecyclerAdapter
    private lateinit var adapterOthers: OtherTrailersRecyclerAdapter
    lateinit var apiService: PostApi
    private var messagesList: MutableList<Trailers> = mutableListOf()
    var fragmentPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TrailersRecyclerAdapter(activity!!)
        adapterOthers = OtherTrailersRecyclerAdapter(activity!!)
        getTrailers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        text_other.setOnClickListener(this)

    }

    override fun onBackStackChanged() {
        if (fragmentPosition == requireFragmentManager().backStackEntryCount) {
            this.onResume()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.text_other -> getOtherTrailers()
        }
    }

    private fun initRecyclerView(list: ArrayList<Trailers>) {
        recyclerViewTrailer.setHasFixedSize(true)
        recyclerViewTrailer.layoutManager = LinearLayoutManager(context)
        recyclerViewTrailer.adapter = adapter
        adapter.setData(list)
    }

    private fun initRecyclerViewOthers(list: ArrayList<Trailers>) {
        recyclerViewTrailerOthers.setHasFixedSize(true)
        recyclerViewTrailerOthers.layoutManager = LinearLayoutManager(context)
        recyclerViewTrailerOthers.adapter = adapterOthers
        adapterOthers.setData(list)
    }

    private fun getTrailers() {
        apiService = PostApi.create(context!!)
        CompositeDisposable().add(
            apiService.getDefaultTrailers(
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

    private fun getOtherTrailers() {
        apiService = PostApi.create(context!!)
        CompositeDisposable().add(
            apiService.getOtherTrailers(
                PreferenceUtils.getAuthorizationToken(context!!)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    recyclerViewTrailerOthers.visibility = View.VISIBLE
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