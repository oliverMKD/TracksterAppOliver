package com.trackster.tracksterapp.ui.login.trailer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.OtherTrailersRecyclerAdapter
import com.trackster.tracksterapp.adapters.TrailersRecyclerAdapter
import com.trackster.tracksterapp.main.MainActivity
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.ui.login.loadDetails.LoadDetailsActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class TrailerActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var listView: RecyclerView
    lateinit var recyclerViewOthers : RecyclerView
    private lateinit var adapter: TrailersRecyclerAdapter
    private lateinit var adapterOthers : OtherTrailersRecyclerAdapter
    lateinit var apiService: PostApi
    private var mContinue: Button? = null
    private var mOthers : TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.trackster.tracksterapp.R.layout.activity_trailer)
        listView = findViewById(R.id.recyclerViewTrailer)
        recyclerViewOthers = findViewById(R.id.recyclerViewTrailerOthers)
        adapter = TrailersRecyclerAdapter(this@TrailerActivity)
        adapterOthers = OtherTrailersRecyclerAdapter(this@TrailerActivity)
        mContinue = findViewById(R.id.button_next_trailer)
        mOthers = findViewById(R.id.text_other)
        getTrailers()
        mContinue!!.setOnClickListener(this)
        mOthers!!.setOnClickListener(this)
    }
    private fun initRecyclerView(list : ArrayList<Trailers>) {
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter
        adapter.setData(list)
    }
    private fun initRecyclerViewOthers(list : ArrayList<Trailers>) {
        recyclerViewOthers.setHasFixedSize(true)
        recyclerViewOthers.layoutManager = LinearLayoutManager(this)
        recyclerViewOthers.adapter = adapterOthers
        adapterOthers.setData(list)
    }
    private fun getTrailers(){
        apiService = PostApi.create(this@TrailerActivity)
        val token = PreferenceUtils.getAuthorizationToken(this)
        CompositeDisposable().add(apiService.getDefaultTrailers(
            PreferenceUtils.getAuthorizationToken(this))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                initRecyclerView(it)
                //                Log.d("station", " "+ it[0].location)
            }, {
                //                showProgress(false)
                Utils.handleApiError(it)
            })
        )
    }
    private fun getOtherTrailers(){
        apiService = PostApi.create(this@TrailerActivity)
        CompositeDisposable().add(apiService.getOtherTrailers(
            PreferenceUtils.getAuthorizationToken(this))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                recyclerViewOthers.visibility = View.VISIBLE
                initRecyclerViewOthers(it)
                //                Log.d("station", " "+ it[0].location)
            }, {
                //                showProgress(false)
                Utils.handleApiError(it)
            })
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_next_trailer -> startActivity(Intent(this@TrailerActivity, MainActivity::class.java))
            R.id.text_other -> getOtherTrailers()
        }
    }
    fun getToastMessage(id : String) {
        Toast.makeText(this@TrailerActivity,"You selected : "+id + " trailer",Toast.LENGTH_LONG).show()
    }
}
