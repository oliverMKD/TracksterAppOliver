package com.trackster.tracksterapp.mainScreen.adapter_Double

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.SelectColorAdapter
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.Colors
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import kotlinx.android.synthetic.main.color_items.view.*

class SelectColorAdapterDouble (private val activity: Activity)  : RecyclerView.Adapter<SelectColorAdapterDouble.LoadsRecyclerViewHolder>()   {

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

        if (truckName == "Apple Green") {
            Glide.with(activity)
                .load(R.drawable.apple_green)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Albescent White") {
            Glide.with(activity)
                .load(R.drawable.albescent_white)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "White") {
            Glide.with(activity)
                .load(R.drawable.white)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        } else if (truckName == "Beige") {
            Glide.with(activity)
                .load(R.drawable.beige)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }

        else if (truckName == "Burgundy") {
            Glide.with(activity)
                .load(R.drawable.burgundy)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Bright Red") {
            Glide.with(activity)
                .load(R.drawable.bright_red)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Chrome White") {
            Glide.with(activity)
                .load(R.drawable.chrome_white)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Chrome Yellow") {
            Glide.with(activity)
                .load(R.drawable.chrome_yellow)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Dark Green") {
            Glide.with(activity)
                .load(R.drawable.dark_green)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Fire Engine Red") {
            Glide.with(activity)
                .load(R.drawable.fire_engine_red)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }else if (truckName == "Gray") {
            Glide.with(activity)
                .load(R.drawable.gray)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Kelly Green") {
            Glide.with(activity)
                .load(R.drawable.kelly_green)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Ripe Lemon") {
            Glide.with(activity)
                .load(R.drawable.ripe_lemon)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Arylide Yellow") {
            Glide.with(activity)
                .load(R.drawable.arylide_yellow)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Fuel Yellow") {
            Glide.with(activity)
                .load(R.drawable.fuel_yellow)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Magenta") {
            Glide.with(activity)
                .load(R.drawable.magenta)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Metallic Copper") {
            Glide.with(activity)
                .load(R.drawable.metallic_copper)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }else if (truckName == "Metallic Gold") {
            Glide.with(activity)
                .load(R.drawable.metallic_gold)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Metallic Bronze") {
            Glide.with(activity)
                .load(R.drawable.metallic_bronze)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Quick Silver") {
            Glide.with(activity)
                .load(R.drawable.quick_silver)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Midnight") {
            Glide.with(activity)
                .load(R.drawable.midnight)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Deep Teal") {
            Glide.with(activity)
                .load(R.drawable.deep_teal)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Ocean Blue") {
            Glide.with(activity)
                .load(R.drawable.ocean_blue)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }

        else if (truckName == "Orange Peel") {
            Glide.with(activity)
                .load(R.drawable.orange_peel)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Purpureus") {
            Glide.with(activity)
                .load(R.drawable.purpureus)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Ripe Plum") {
            Glide.with(activity)
                .load(R.drawable.ripe_plum)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Royal Blue") {
            Glide.with(activity)
                .load(R.drawable.royal_blue)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Saddle Brown") {
            Glide.with(activity)
                .load(R.drawable.saddle_brown)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Blue Sapphire") {
            Glide.with(activity)
                .load(R.drawable.blue_sapphire)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }
        else if (truckName == "Light Sky Blue") {
            Glide.with(activity)
                .load(R.drawable.light_sky_blue)
                .apply(RequestOptions.circleCropTransform()).centerInside()
                .into(holder.image)
        }

        holder.itemView.setOnClickListener {
            if (color != null) {
               (activity as MainScreenActivity).getSelectedColorDouble(color.name)
            }
        }

    }


}