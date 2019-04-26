package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.model.Trucks
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import kotlinx.android.synthetic.main.fragment_select_manufactor.view.*

class SelectTruckAdapter(private val activity: Activity) :
    RecyclerView.Adapter<SelectTruckAdapter.LoadsRecyclerViewHolder>() {


    private val list: MutableList<Trucks?> = arrayListOf()

    inner class LoadsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.image_manufactor!!

    }

    fun setData(list: MutableList<Trucks>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmptyState() = list.isEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadsRecyclerViewHolder =     LoadsRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_select_manufactor,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LoadsRecyclerViewHolder, position: Int) {
        val truck = list[position]
        val truckName = truck!!.name

        if (truckName == "Ford") {
            Glide.with(activity)
                .load(R.drawable.ford)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Chevrolet") {
            Glide.with(activity)
                .load(R.drawable.chevrolet)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Freightliner") {
            Glide.with(activity)
                .load(R.drawable.freightliner)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "GMC") {
            Glide.with(activity)
                .load(R.drawable.gmc)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "International") {
            Glide.with(activity)
                .load(R.drawable.international)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Isuzu") {
            Glide.with(activity)
                .load(R.drawable.isuzu)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Kenworth") {
            Glide.with(activity)
                .load(R.drawable.kenworth)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Mack") {
            Glide.with(activity)
                .load(R.drawable.mack)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Peterbilt") {
            Glide.with(activity)
                .load(R.drawable.peterbilt)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Sterling") {
            Glide.with(activity)
                .load(R.drawable.sterling)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Volvo") {
            Glide.with(activity)
                .load(R.drawable.volvo)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Western Star") {
            Glide.with(activity)
                .load(R.drawable.western_star)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        holder.itemView.setOnClickListener {
            if (truck != null) {
                (activity as SelectTrailerActivity).getSelectedTruck(truck.id)
            }
        }
    }
}