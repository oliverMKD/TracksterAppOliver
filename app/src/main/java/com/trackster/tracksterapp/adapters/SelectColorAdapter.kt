package com.trackster.tracksterapp.adapters


import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.model.Colors
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import kotlinx.android.synthetic.main.color_items.view.*

class SelectColorAdapter(private val activity: Activity)  : RecyclerView.Adapter<SelectColorAdapter.LoadsRecyclerViewHolder>()   {

    private val list: MutableList<Colors> = arrayListOf()

    inner class LoadsRecyclerViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val image = view.image_color!!
    }

    fun setData(list: MutableList<Colors>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun isEmptyState() = list.isEmpty()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):  LoadsRecyclerViewHolder = LoadsRecyclerViewHolder (
        LayoutInflater.from(p0.context).inflate(     R.layout.color_items,    p0,     false
        )

    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LoadsRecyclerViewHolder, position: Int) {
        val color = list[position]
        val truckName = color!!.name

        if (truckName == "red") {
            Glide.with(activity)
                .load(R.drawable.red_cicrle)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "black") {
            Glide.with(activity)
                .load(R.drawable.black_circle)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "gray") {
            Glide.with(activity)
                .load(R.drawable.yellow_circle)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "blue") {
            Glide.with(activity)
                .load(R.drawable.blue_circle)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        holder.itemView.setOnClickListener {
            if (color != null) {
                (activity as SelectTrailerActivity).getSelectedColor()
            }
        }

    }


}