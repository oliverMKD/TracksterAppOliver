package com.trackster.tracksterapp.ui.login.trailer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ExpandableListView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.ExpandListAdapter
import com.trackster.tracksterapp.listeners.TotalListener
import com.trackster.tracksterapp.main.MainActivity


class TrailerActivity : AppCompatActivity(), TotalListener,View.OnClickListener {


    override fun onTotalChanged(sum: Int) {
        Log.d("proba", "proba")
    }

    override fun expandGroupEvent(groupPosition: Int, isExpanded: Boolean) {
        if (isExpanded)
            listView.collapseGroup(groupPosition)
        else
            listView.expandGroup(groupPosition); }

    lateinit var listView: ExpandableListView
    private var mContinue: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.trackster.tracksterapp.R.layout.activity_trailer)
        listView = findViewById<ExpandableListView>(com.trackster.tracksterapp.R.id.expandable_list)
        val adapter = ExpandListAdapter(this)
        adapter.setmListener(this)
        listView.setAdapter(adapter)
        mContinue = findViewById(R.id.button_next_trailer)
        mContinue!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_next_trailer -> startActivity(Intent(this@TrailerActivity, MainActivity::class.java))
        }
    }
}
