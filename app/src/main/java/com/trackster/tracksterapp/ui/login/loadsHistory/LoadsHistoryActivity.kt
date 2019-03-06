package com.trackster.tracksterapp.ui.login.loadsHistory

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.LoadsRecyclerAdapter
import com.trackster.tracksterapp.model.Shipment

class LoadsHistoryActivity : AppCompatActivity() {

    private lateinit var adapter: LoadsRecyclerAdapter
    private var messagesList: MutableList<Shipment> = mutableListOf()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loads_history)
        recyclerView = findViewById(R.id.recyclerViewHistory)
        adapter = LoadsRecyclerAdapter(this)
        initRecyclerView()
    }
    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messagesList.add(0, (Shipment("Da vidime sto ke vrakja API-to za istorija", "", "", 100)))
        messagesList.add(1, (Shipment("Da vidime sto ke vrakja API-to za istorija", "sofer2", "golem paket", 100)))
        messagesList.add(2, (Shipment("Da vidime sto ke vrakja API-to za istorija", "sofer3", "golem paket", 100)))
        messagesList.add(3, (Shipment("Da vidime sto ke vrakja API-to za istorija", "sofer4", "golem paket", 100)))
        messagesList.add(4, (Shipment("Da vidime sto ke vrakja API-to za istorija", "sofer5", "golem paket", 100)))
        recyclerView.adapter = adapter
        adapter.setData(messagesList)
    }
}
