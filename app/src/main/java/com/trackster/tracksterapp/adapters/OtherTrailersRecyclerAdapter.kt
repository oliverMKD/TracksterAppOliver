package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.main.MainActivity
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.network.responce.ChatResponse
import com.trackster.tracksterapp.ui.login.trailer.TrailerActivity
import kotlinx.android.synthetic.main.loads_item.view.*
import java.util.*

class OtherTrailersRecyclerAdapter(private val activity: Activity) : RecyclerView.Adapter<OtherTrailersRecyclerAdapter.LoadsRecyclerViewHolder>() {




    private val list: MutableList<Trailers?> = arrayListOf()

    inner class LoadsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description = view.text_description!!
        val price = view.text_price!!
        val text_plannedDestinationTime = view.text_plannedDestinationTime!!
        val text_plannedPickupTime = view.text_plannedPickupTime!!
        val text_company_name = view.text_company_name!!
        val text_plannedPickupAddress = view.text_plannedPickupAddress!!
        val text_planneddestinationAddress = view.text_planneddestinationAddress!!
    }

    fun setData(list: MutableList<Trailers>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
    fun addData(listUpdate: ArrayList<Trailers>) {
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

//        val loadPrice = "price = " + load!!.price
        val loadDescription = "description : " + load!!.name

//        holder.description.text = loadPrice.toString()
        holder.price.text = loadDescription
//        holder.text_plannedPickupTime.text = "Planed pickup time : " + load.plannedPickupTime
//        holder.text_plannedDestinationTime.text = "Planed destination time : " + load.plannedDestinationTime
        holder.text_company_name.visibility = View.GONE
        holder.text_plannedPickupAddress.visibility = View.GONE
        holder.text_planneddestinationAddress.visibility = View.GONE

//
        holder.itemView.setOnClickListener {
            if (load != null) {
                (activity as TrailerActivity).getToastMessage(load.name)
            }
        }
    }
}