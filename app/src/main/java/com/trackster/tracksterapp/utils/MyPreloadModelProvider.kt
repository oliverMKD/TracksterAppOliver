package com.trackster.tracksterapp.utils

import android.content.Context
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import java.util.*
import kotlin.collections.ArrayList

class MyPreloadModelProvider(val listUrls: ArrayList<String>, val context: Context) :
    ListPreloader.PreloadModelProvider<Any> {


    override fun getPreloadRequestBuilder(item: Any): RequestBuilder<*>? {
        return Glide.with(context)
            .load(item)    }

    override fun getPreloadItems(position: Int): MutableList<Any> {
        val url = listUrls[position]
        if (TextUtils.isEmpty(url)) {
            return Collections.emptyList()
        }
        return Collections.singletonList(url)
    }



}