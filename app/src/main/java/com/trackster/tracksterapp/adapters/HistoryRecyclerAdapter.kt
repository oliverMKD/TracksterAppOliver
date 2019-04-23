package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.model.ChatResponse
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import kotlinx.android.synthetic.main.loads_item.view.*
import kotlinx.android.synthetic.main.recycler_history.view.*

class HistoryRecyclerAdapter (private  val activity: Activity) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryRecyclerViewHolder>() {


    private val list: MutableList<ChatResponse?> = arrayListOf()


    inner class HistoryRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description = view.routeName!!
        val price = view.value!!
        val miles = view.miles!!
        val date = view.data!!
        val hisbtn = view.hisbtn!!

    }
//    fun setData(list: MutableList<Trailers>) {
//        this.list.clear()
//        this.list.addAll(list)
//        notifyDataSetChanged()
//    }

//    fun addData(listUpdate: ArrayList<Trailers>) {
//        val start = list.size
//        var end = listUpdate.size
//
//        val incStart = start + list.size / 10 + 1
//
//        if (listUpdate.size >= 10) {
//            end += 2
//        }
//
//        this.list.addAll(listUpdate)
//        notifyItemRangeChanged(incStart, end)
//    }
//
//    fun isEmptyState() = list.isEmpty()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HistoryRecyclerViewHolder =
        HistoryRecyclerViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.recycler_history,p0,false))


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryRecyclerViewHolder, position: Int) {

        val load = list[position]

//        val description = load.routeName!!
//        val price = load.value!!
//        val miles = load.miles!!
//        val date = load.data!!
//        val hisbtn = load.hisbtn!!


    }
}