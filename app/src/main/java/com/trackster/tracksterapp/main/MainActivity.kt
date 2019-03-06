package com.trackster.tracksterapp.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.LoadsRecyclerAdapter
import com.trackster.tracksterapp.base.BaseBottomItemMenuFragment
import com.trackster.tracksterapp.model.Shipment
import com.trackster.tracksterapp.ui.login.loadDetails.LoadDetailsActivity
import com.trackster.tracksterapp.ui.login.loadsHistory.LoadsHistoryActivity
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity(), View.OnClickListener {


//    private val settingsFragment: SettingsFragment = SettingsFragment.newInstance()
//    private val loadsFragment: LoadsFragment = LoadsFragment.newInstance("")

    var activeMenuFragment: BaseBottomItemMenuFragment? = null
//    private lateinit var bottomNavImageList: List<ImageView>
    private lateinit var adapter: LoadsRecyclerAdapter
    private var messagesList: MutableList<Shipment> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mHistory: ImageView
    private lateinit var mLocation: ImageView
    private lateinit var mSettings: ImageView




    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loads)
        mHistory = findViewById(R.id.menuButtonEventsNew)
        mLocation = findViewById(R.id.menuButtonNotificationsNew)
        mSettings = findViewById(R.id.menuButtonUserNew)
        mHistory.setOnClickListener (this@MainActivity)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = LoadsRecyclerAdapter(this)
        initRecyclerView()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.menuButtonEventsNew -> openHistoryActivity()
        }
    }
    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messagesList.add(0, (Shipment("prvi", "sofer1", "golem paket", 100)))
        messagesList.add(1, (Shipment("vtori", "sofer2", "golem paket", 100)))
        messagesList.add(2, (Shipment("treti", "sofer3", "golem paket", 100)))
        messagesList.add(3, (Shipment("cetvrti", "sofer4", "golem paket", 100)))
        messagesList.add(4, (Shipment("petti", "sofer5", "golem paket", 100)))
        recyclerView.adapter = adapter
        adapter.setData(messagesList)
    }

    fun openLoadDetailsActivity() {
        startActivity(Intent(this@MainActivity, LoadDetailsActivity::class.java))
    }

    fun openHistoryActivity() {
        val intent = Intent(this@MainActivity, LoadsHistoryActivity::class.java)
//        startActivity(Intent(this@MainActivity, LoadsHistoryActivity::class.java))
        startActivity(intent)

    }


//    override fun getLayoutId(): Int = R.layout.fragment_loads
//
//    override fun isUpNavigationEnabled(): Boolean = false
//
//    override fun getToolbar(): Toolbar? = null
//
//    override fun hasToolbar(): Boolean = false
//
//    override fun hasTitle(): Boolean = false

    private fun setNavigationEvents() {

//        menuButtonHome.setOnClickListener {
//            //            openLoadsFragment()
//            setSelectedItem(it.id)
//            menuButtonHome.setColorFilter(
//                menuButtonHome.getContext().getResources().getColor(R.color.colorPurpleLight),
//                PorterDuff.Mode.SRC_ATOP
//            )
//        }
//
//        menuButtonEvents.setOnClickListener {
//            openHistoryActivity()
////            setSelectedItem(menuButtonHome.id)
////            menuButtonEvents.setColorFilter(
////                menuButtonEvents.getContext().getResources().getColor(R.color.colorPurpleLight),
////                PorterDuff.Mode.SRC_ATOP
////            )
////            menuButtonHome.setColorFilter(
////                menuButtonHome.getContext().getResources().getColor(R.color.colorGrayLight),
////                PorterDuff.Mode.SRC_ATOP
////            )
//        }

//        menuButtonNotifications.setOnClickListener {
//            //            openGuestListWalletFragment()
//            setSelectedItem(it.id)
//        }
//
//        menuButtonUser.setOnClickListener {
//            //            openProfileFragment(false)
//            setSelectedItem(menuButtonUser.id)
//        }
    }

//    public fun setSelectedItem(id: Int) {
//        bottomNavImageList.asSequence().filter { it.id != id }.map { x -> x.setColorFilter(Color.GRAY) }.toList()
//        bottomNavImageList.find { x -> x.id == id }?.setColorFilter(resources.getColor(R.color.colorPurpleLight))
//    }
}

