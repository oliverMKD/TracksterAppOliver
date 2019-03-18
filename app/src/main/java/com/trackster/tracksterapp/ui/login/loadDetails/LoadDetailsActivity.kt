package com.trackster.tracksterapp.ui.login.loadDetails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoadDetailsActivity : AppCompatActivity() {

    private lateinit var mText: TextView
    lateinit var apiService: PostApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_details)

        val intent = intent
        if (intent.hasExtra("id")){
            Log.d("intent", intent.getStringExtra("id") )
        }
        getChatById(intent.getStringExtra("id"))

        mText = findViewById(R.id.textDetali)
    }
    private fun getChatById(id : String){
        apiService = PostApi.create(this@LoadDetailsActivity)
        CompositeDisposable().add(apiService.getChatById(
            PreferenceUtils.getAuthorizationToken(this),id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("details",""+ it)
                mText.text = "Description : " +it.description + "\n" + "plannedPickupTime : " + it.plannedPickupTime +
                        "\n" + "plannedDestinationTime : " + it.plannedDestinationTime + "\n" + "price : " + it.price

                //                Log.d("station", " "+ it[0].location)
            }, {
                //                showProgress(false)
                Utils.handleApiError(it)
            })
        )
    }
}