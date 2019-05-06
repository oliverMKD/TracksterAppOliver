package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.responce.ChatResponse
import kotlinx.android.synthetic.main.recycler_history.view.*

class HistoryRecyclerAdapter (private  val activity: Activity) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryRecyclerViewHolder>() {


    private val list: MutableList<ChatResponse?> = arrayListOf()


    inner class HistoryRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routename = view.routeName!!
        val price = view.value!!
        val miles = view.miles!!
        val date = view.data!!
        val hisbtn = view.hisbtn!!

    }
    fun setData(list: MutableList<ChatResponse>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HistoryRecyclerViewHolder =
        HistoryRecyclerViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.recycler_history,p0,false))


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryRecyclerViewHolder, position: Int) {

        val load = list[position]
        holder.date.text = load?.plannedDestinationTime
        holder.routename.text = load!!.description
        holder.price.text = load!!.price.toString()
        holder.miles.text = load!!.distance.toString()
    }
}