package com.trackster.tracksterapp.main

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.LoadsRecyclerAdapter
import com.trackster.tracksterapp.base.BaseBottomItemMenuFragment
import com.trackster.tracksterapp.base.BaseMainActivity
import com.trackster.tracksterapp.main.fragments.LoadsFragment
import com.trackster.tracksterapp.main.fragments.SettingsFragment
import com.trackster.tracksterapp.model.Shipment
import com.trackster.tracksterapp.ui.login.loadDetails.LoadDetailsActivity
import com.trackster.tracksterapp.ui.login.loadsHistory.LoadsHistoryActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.bottom_navigation_layout.*
import kotlinx.android.synthetic.main.fragment_loads.*

class MainActivity : BaseMainActivity(), View.OnClickListener {


    companion object {
        const val FRAGMENT_CHOOSE_MORE_ITEMS = "fragment_choose_more_items"
    }

//    private val settingsFragment: SettingsFragment = SettingsFragment.newInstance()
//    private val loadsFragment: LoadsFragment = LoadsFragment.newInstance("")

    var activeMenuFragment: BaseBottomItemMenuFragment? = null
    private lateinit var bottomNavImageList: List<ImageView>
    private lateinit var adapter: LoadsRecyclerAdapter
    private var messagesList: MutableList<Shipment> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private var mHistory: ImageView? = null


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.menuButtonEvents -> openHistoryActivity()
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        bottomNavImageList = listOf(menuButtonHome, menuButtonEvents, menuButtonNotifications, menuButtonUser)
//        setNavigationEvents()

        mHistory = findViewById(R.id.menuButtonEvents)
        mHistory!!.setOnClickListener(this)
        menuButtonEvents.setOnClickListener { openHistoryActivity() }
//        setSelectedItem(R.id.menuButtonHome)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = LoadsRecyclerAdapter(this)
        initRecyclerView()
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
        startActivity(Intent(this@MainActivity, LoadsHistoryActivity::class.java))

    }


    override fun getLayoutId(): Int = R.layout.fragment_loads

    override fun isUpNavigationEnabled(): Boolean = false

    override fun getToolbar(): Toolbar? = null

    override fun hasToolbar(): Boolean = false

    override fun hasTitle(): Boolean = false

    private fun setNavigationEvents() {

//        menuButtonHome.setOnClickListener {
//            //            openLoadsFragment()
//            setSelectedItem(it.id)
//            menuButtonHome.setColorFilter(
//                menuButtonHome.getContext().getResources().getColor(R.color.colorPurpleLight),
//                PorterDuff.Mode.SRC_ATOP
//            )
//        }

        menuButtonEvents.setOnClickListener {
            openHistoryActivity()
//            setSelectedItem(menuButtonHome.id)
//            menuButtonEvents.setColorFilter(
//                menuButtonEvents.getContext().getResources().getColor(R.color.colorPurpleLight),
//                PorterDuff.Mode.SRC_ATOP
//            )
//            menuButtonHome.setColorFilter(
//                menuButtonHome.getContext().getResources().getColor(R.color.colorGrayLight),
//                PorterDuff.Mode.SRC_ATOP
//            )
        }

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

    public fun setSelectedItem(id: Int) {
        bottomNavImageList.asSequence().filter { it.id != id }.map { x -> x.setColorFilter(Color.GRAY) }.toList()
        bottomNavImageList.find { x -> x.id == id }?.setColorFilter(resources.getColor(R.color.colorPurpleLight))
    }
}

