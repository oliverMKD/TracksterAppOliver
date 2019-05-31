package com.trackster.tracksterapp.mainScreen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_details.*
import org.json.JSONArray


class DetailsLoad : BaseFragment() {

    lateinit var apiService: PostApi
    var compositeDisposableContainer = CompositeDisposable()
    private lateinit var Id: String

    private lateinit var description: String
    private var mPrice: TextView? = null
    private lateinit var price: String
    private var mNewPrice: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var s = PreferenceUtils.getString(context!!)
        val array = JSONArray(s)
        for (i in 0 until array.length()) {
            val row = array.getJSONObject(i)
            description = row.getString("description")
            price = row.getString("price")
//            mDescr!!.text = description
//            mPrice!!.setText(priceee)
        }


    }

    @SuppressLint("LogNotTimber")
    private fun getLoadsInfo() {
        apiService = PostApi.create(context!!)
        compositeDisposableContainer.add(
            apiService.getDetails(
                PreferenceUtils.getAuthorizationToken(context!!)
            ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                {
                    Id = it[0].id
                    desc.setText(it[0].description)
//                    price.setText(it[0].price)
                    broker_name.text = it[0].broker.firstName
                    broker_last_name.text = it[0].broker.lastName
                }, {
                    Log.d("test", "error" + it.localizedMessage)
                }
            )
        )
    }

    override fun onBackStackChanged() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPrice = view.findViewById(R.id.desc)
        mPrice!!.text = description
        mNewPrice = view.findViewById(R.id.price)
        mNewPrice!!.text = price
    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = com.trackster.tracksterapp.R.layout.fragment_details

    companion object {
        fun newInstance() = DetailsLoad()
    }

    override fun onDestroy() {
        (activity as MainScreenActivity).show()
        super.onDestroy()
    }
}