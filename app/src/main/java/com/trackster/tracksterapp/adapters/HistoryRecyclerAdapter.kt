package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.History
import kotlinx.android.synthetic.main.recycler_history.view.*
import android.text.method.TextKeyListener.clear



class HistoryRecyclerAdapter(private val activity: Activity) :
    RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryRecyclerViewHolder>() {


    private val list: MutableList<History?> = arrayListOf()


    inner class HistoryRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routename = view.routeName!!
        val price = view.value!!
        val miles = view.miles!!
        val date = view.data!!
        val hisbtn = view.hisbtn!!

    }

    fun setData(list: MutableList<History>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear() // clear list
        notifyDataSetChanged() // let your adapter know about the changes and reload view.
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HistoryRecyclerViewHolder =
        HistoryRecyclerViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.recycler_history, p0, false))


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryRecyclerViewHolder, position: Int) {

        val load = list[position]

        val loadPrice = load!!.price
        val loadDescription = load.description
        val loadDistance = load.distance
        val id = load.id

        holder.routename.text = loadDescription
        holder.price.text = loadPrice
        holder.miles.text = loadDistance
        holder.itemView.setOnClickListener {
            if (load != null) {
                (activity as MainScreenActivity).getSelectedLoad(id)
            }
        }

    }
}