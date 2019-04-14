package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.main.MainActivity
import com.trackster.tracksterapp.model.Shipment
import kotlinx.android.synthetic.main.loads_item.view.*
import java.util.*

class LoadsRecyclerAdapter(private val activity: Activity) : RecyclerView.Adapter<LoadsRecyclerAdapter.LoadsRecyclerViewHolder>() {




    private val list: MutableList<Shipment?> = arrayListOf()

    inner class LoadsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description = view.image_select_trailer!!
        val price = view.text_price
    }

    fun setData(list: MutableList<Shipment>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
    fun addData(listUpdate: ArrayList<Shipment>) {
        val start = list.size
        var end = listUpdate.size

        val incStart = start + list.size / 10 + 1

        if (listUpdate.size >= 10) {
            end += 2
        }

        this.list.addAll(listUpdate)
        notifyItemRangeChanged(incStart, end)
    }

    fun isEmptyState() = list.isEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadsRecyclerViewHolder =
            LoadsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loads_item, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LoadsRecyclerViewHolder, position: Int) {
        val load = list[position]

        val loadPrice = load!!.price
        val loadDescription = load.description

//        holder.description.text = loadPrice.toString()
        holder.price.text = loadDescription

        holder.itemView.setOnClickListener {
            if (load != null) {
                (activity as MainActivity).openLoadDetailsActivity()
            }
        }
    }
}