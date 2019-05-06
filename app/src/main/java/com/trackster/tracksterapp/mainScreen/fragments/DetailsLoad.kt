package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.ChatResponse
import com.trackster.tracksterapp.model.User
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_profil.*

class DetailsLoad :  BaseFragment() {

    lateinit var apiService: PostApi
    private var userInfo: MutableList<ChatResponse> = mutableListOf()
    var fragmentPosition: Int = 0
    var compositeDisposableContainer = CompositeDisposable()
    private lateinit var Id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserInfo()

    }

    private fun getUserInfo(){

        apiService= PostApi.create(context!!)
        compositeDisposableContainer.add(
            apiService.getDetails(
                PreferenceUtils.getAuthorizationToken(context!!)
            ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                {
                    Id = it[0].id
                    desc.setText(it[0] .description)
                    price.setText(it[0].price)
                    broker_name.text = it[0].broker.firstName
                    broker_last_name.text = it[0].broker.lastName
//                    orignin_name.setText(it[0].pickupAddresses.street)
//                    delivery_house.setText(it[0].pickupAddresses.street)
//                    pickup.setText(it[0].pickupAddresses.plannedTime)
//                    delivery_date.setText(it[0].pickupAddresses.plannedTime)



                } ,{
                    Log.d("test","error"+ it.localizedMessage)
                }
            )
        )
    }

    override fun onBackStackChanged() {
    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.fragment_details

    companion object {
        fun newInstance() = DetailsLoad()
    }

    override fun onDestroy() {
        (activity as MainScreenActivity).show()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}