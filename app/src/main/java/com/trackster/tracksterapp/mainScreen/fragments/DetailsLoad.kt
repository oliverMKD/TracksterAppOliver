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
import com.trackster.tracksterapp.model.PiskupAddresses
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.DateFormat
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
    private var mDesc: TextView? = null
    private lateinit var price: String
    private var mPrice: TextView? = null
    private var mfullName: TextView? = null
    private var mOrigin: TextView? = null
    private var mPickupTime: TextView? = null
    private var mDelivery: TextView? = null
    private var mDeliveryTime: TextView? = null
    private lateinit var brokerFirstName: String
    private lateinit var brokerLastName: String
    private lateinit var fullName: String
    private lateinit var originName: String
    private lateinit var deliveryName: String
    private lateinit var pickupTime: String
    private lateinit var deliveryTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var s = PreferenceUtils.getString(context!!)
        val array = JSONArray(s)
        for (i in 0 until array.length()) {
            val row = array.getJSONObject(i)
            val idS = row.getString("id")
            if (idS == PreferenceUtils.getChatId(context!!)) {
                description = row.getString("description")
                price = row.getString("price")
                val broker = row.getJSONObject("broker")
                brokerFirstName = broker.getString("firstName")
                brokerLastName = broker.getString("lastName")
                fullName = brokerFirstName + " " + brokerLastName
                val na = row.getJSONArray("pickupAddresses")
                val pa = row.getJSONArray("destinationAddresses")
                for (x in 0 until na.length()) {
                    val row11 = na.getJSONObject(x)
                    originName = row11.getString("formatted")
                    pickupTime = row11.getString("plannedTime")
                }
                for (z in 0 until pa.length()) {
                    val row12 = pa.getJSONObject(z)

                    deliveryName = row12.getString("formatted")
                    deliveryTime = row12.getString("plannedTime")

                }
            }
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
        mDesc = view.findViewById(R.id.desc)
        mDesc!!.text = description
        mPrice = view.findViewById(R.id.price)
        mPrice!!.text = price
        mfullName = view.findViewById(R.id.broker_name)
        mfullName!!.text = fullName
        mOrigin = view.findViewById(R.id.orignin_name)
        mOrigin!!.text = originName
        mPickupTime = view.findViewById(R.id.pickup)
        var messageDate = DateFormat.formatDateDelivery(pickupTime, DateFormat.DATE_FORMAT_MESSAGE_DELIVERY)
        mPickupTime!!.text = messageDate
        mDelivery = view.findViewById(R.id.delivery_house)
        mDelivery!!.text = deliveryName
        mDeliveryTime = view.findViewById(R.id.delivery_date)
        var messageDateDelivery = DateFormat.formatDateDelivery(deliveryTime, DateFormat.DATE_FORMAT_MESSAGE_DELIVERY)
        mDeliveryTime!!.text = messageDateDelivery


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