package com.trackster.tracksterapp.ui.login.loadsHistory

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.LoadsRecyclerAdapter
import com.trackster.tracksterapp.model.Shipment
import com.trackster.tracksterapp.network.responce.ChatResponse

class LoadsHistoryActivity : AppCompatActivity() {

    private lateinit var adapter: LoadsRecyclerAdapter
    private var messagesList: MutableList<ChatResponse> = mutableListOf()
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

        recyclerView.adapter = adapter
        adapter.setData(messagesList)
    }
}
