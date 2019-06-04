package com.trackster.tracksterapp.mainScreen.fragments

import android.annotation.SuppressLint
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
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.requests.UserRequest
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profil.*
import org.json.JSONArray
import org.json.JSONObject

class ProfileSettings : BaseFragment(), View.OnClickListener {

    var fullname = ""
    var lastName = ""
    var firstName = ""
    lateinit var apiService: PostApi
    lateinit var driver: JSONObject
    private lateinit var emailJson: String
    private lateinit var firstNameJson: String
    private lateinit var lastNameJson: String
    private lateinit var phone: String
   private lateinit var profilePic : String


    var compositeDisposableContainer = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val s = PreferenceUtils.getString(context!!)
        val array = JSONArray(s)
        for (i in 0 until array.length()) {
            val row = array.getJSONObject(i)
            driver = row.getJSONObject("driver")
            firstNameJson = driver.getString("firstName")
            lastNameJson = driver.getString("lastName")
            phone = driver.getString("phone")
            emailJson = driver.getString("email")
            profilePic = driver.getString("image")
        }
    }

    @SuppressLint("LogNotTimber")
    private fun updateUser() {

        val name1 = name.text.toString()

        if (name1.split(("\\w+").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray().size > 1) {
            lastName = name1.substring(name1.lastIndexOf(" ") + 1)
            firstName = name1.substring(0, name1.lastIndexOf(' '))
        } else {
            firstName = name1
        }
        var middle = " "
        fullname = firstName + middle + lastName

        apiService = PostApi.create(context!!)
        CompositeDisposable().add(
            apiService.updateUser(
                PreferenceUtils.getAuthorizationToken(context!!),
                UserRequest(fullname, PreferenceUtils.getUserId(context!!), email.text.toString())
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {
                    Log.d("pane", "error")
                })
        )
    }

    override fun onBackStackChanged() {
    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.fragment_profil

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainScreenActivity).show()
    }

    companion object {
        fun newInstance() = ProfileSettings()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.color -> {
                (activity as MainScreenActivity).openSelectColorFragmentDouble()
            }
            R.id.type_truck -> {
                (activity as MainScreenActivity).openSelectTruckFragmentDouble()
            }
            R.id.save_changes -> {
                updateUser()
                (activity as MainScreenActivity).onBackPressed()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        color.setOnClickListener(this)
        type_truck.setOnClickListener(this)
        save_changes.setOnClickListener(this)
        name.setText("$firstNameJson$lastNameJson")
        email.setText(emailJson)
        phone_number.setText(phone)
        Glide.with(this@ProfileSettings)
            .load(profilePic)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_image)
    }
}