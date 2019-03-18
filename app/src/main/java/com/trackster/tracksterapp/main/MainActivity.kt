package com.trackster.tracksterapp.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.LoadsRecyclerAdapter
import com.trackster.tracksterapp.base.BaseBottomItemMenuFragment
import com.trackster.tracksterapp.model.Shipment
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.responce.ChatResponse
import com.trackster.tracksterapp.ui.login.loadDetails.LoadDetailsActivity
import com.trackster.tracksterapp.ui.login.loadsHistory.LoadsHistoryActivity
import com.trackster.tracksterapp.ui.login.maps.MapsActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils.handleApiError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), View.OnClickListener {


//    private val settingsFragment: SettingsFragment = SettingsFragment.newInstance()
//    private val loadsFragment: LoadsFragment = LoadsFragment.newInstance("")

    var activeMenuFragment: BaseBottomItemMenuFragment? = null
//    private lateinit var bottomNavImageList: List<ImageView>
    private lateinit var adapter: LoadsRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mHistory: ImageView
    private lateinit var mLocation: ImageView
    private lateinit var mSettings: ImageView
    lateinit var apiService: PostApi
    private lateinit var idMaps: String




    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loads)
        mHistory = findViewById(R.id.menuButtonEventsNew)
        mLocation = findViewById(R.id.menuButtonNotificationsNew)
        mSettings = findViewById(R.id.menuButtonUserNew)

        mHistory.setOnClickListener (this@MainActivity)
        mLocation.setOnClickListener (this@MainActivity)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = LoadsRecyclerAdapter(this)
//        initRecyclerView()
        getChats()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.menuButtonEventsNew -> openHistoryActivity()
            R.id.menuButtonNotificationsNew -> openMapsActivity(idMaps)
        }
    }

    private fun openMapsActivity(id : String) {
        val i = Intent(this@MainActivity,MapsActivity::class.java)
        i.putExtra("idMaps",id)
        startActivity(i)
    }

    private fun getChats(){
        apiService = PostApi.create(this@MainActivity)
        val token = PreferenceUtils.getAuthorizationToken(this)
        CompositeDisposable().add(apiService.getChats(
            PreferenceUtils.getAuthorizationToken(this))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                initRecyclerView(it)
                idMaps = it[0].id

                //                Log.d("station", " "+ it[0].location)
            }, {
                //                showProgress(false)
                handleApiError(it)
            })
        )
    }

    private fun initRecyclerView(list : ArrayList<ChatResponse>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setData(list)
    }

    fun openLoadDetailsActivity(id : String) {
        val intent = Intent(this@MainActivity, LoadDetailsActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }

    fun openHistoryActivity() {
        val intent = Intent(this@MainActivity, LoadsHistoryActivity::class.java)
//        startActivity(Intent(this@MainActivity, LoadsHistoryActivity::class.java))
        startActivity(intent)

    }
}

