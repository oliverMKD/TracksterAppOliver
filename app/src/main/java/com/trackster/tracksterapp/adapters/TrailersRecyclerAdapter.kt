package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import com.trackster.tracksterapp.ui.login.trailer.TrailerActivity
import kotlinx.android.synthetic.main.loads_item.view.*

class TrailersRecyclerAdapter(private val activity: Activity) :
    RecyclerView.Adapter<TrailersRecyclerAdapter.LoadsRecyclerViewHolder>() {


    private val list: MutableList<Trailers?> = arrayListOf()

    inner class LoadsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description = view.image_select_trailer!!
        val price = view.text_price!!
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
        val loadDescription = load!!.name
        holder.price.text = loadDescription


        if (loadDescription == "Dryvan") {
            Glide.with(activity)
                .load(R.drawable.dryvan)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        } else if (loadDescription == "Flatbed") {
            Glide.with(activity)
                .load(R.drawable.flatbed)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }
        else if (loadDescription == "Reefer") {
            Glide.with(activity)
                .load(R.drawable.reefer)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }
        else if (loadDescription == "Agriculture Equipment Transport Trailers") {
            Glide.with(activity)
                .load(R.drawable.agriculture_equipment_transport_railers)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }
        else if (loadDescription == "Belt Trailers") {
            Glide.with(activity)
                .load(R.drawable.belt_trailers)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }

        else if (loadDescription == "Beverage Trailers") {
            Glide.with(activity)
                .load(R.drawable.beverage_trailers)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }
        else if (loadDescription == "Blade / Tower Trailers") {
            Glide.with(activity)
                .load(R.drawable.blade_tower_trailers)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }

        else if (loadDescription == "Boom Dolly Trailers") {
            Glide.with(activity)
                .load(R.drawable.boom_dolly_trailers)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }

        else if (loadDescription == "Enclosed") {
            Glide.with(activity)
                .load(R.drawable.enclosed)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.description);
        }


        holder.itemView.setOnClickListener {
            if (load != null) {
                (activity as SelectTrailerActivity).getSelectedTrailer(load.name)
            }
        }
    }
}