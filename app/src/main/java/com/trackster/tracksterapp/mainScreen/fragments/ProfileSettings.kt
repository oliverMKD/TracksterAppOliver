package com.trackster.tracksterapp.mainScreen.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseFragment
import com.trackster.tracksterapp.mainScreen.MainScreenActivity

import com.trackster.tracksterapp.model.User
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.requests.FbLoginRequest
import com.trackster.tracksterapp.network.requests.UserRequest
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfileSettings  : BaseFragment(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.color ->{updateUser()}
//                (activity as MainScreenActivity).onBackPressed()}

        }
    }



    lateinit var apiService: PostApi
    private var userInfo: MutableList<User> = mutableListOf()
    var fragmentPosition: Int = 0
    var compositeDisposableContainer = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUserInfo()

    }

    private fun getUserInfo(){

        apiService= PostApi.create(context!!)
        compositeDisposableContainer.add(
            apiService.getInfoUser(PreferenceUtils.getAuthorizationToken(context!!)
            ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                {

                    name.setText(it.body()!!.firstName)
                    email.setText(it.body()!!.email)
                    phone_number.setText(it.body()!!.phone)
                    Glide.with(activity!!)
                        .load(it.body()!!.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_image)



                } ,{
                    Log.d("test","error"+ it.localizedMessage)
                }

            )

        )


    }



    private fun updateUser() {
        apiService = PostApi.create(context!!)
        CompositeDisposable().add(

            apiService.updateUser( PreferenceUtils.getAuthorizationToken(context!!), UserRequest("Pane",3, "5c6c76eb17d4421770ae188d","broker@brokertest.com") )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                    startActivity(Intent(this@LoginPane, TrailerActivity::class.java))

                }, {


                    Log.d("panePeder", "error")
                })
        )
    }

    override fun onBackStackChanged() {


    }

    override fun getProgressBar(): ProgressBar? = null

    override fun getLayoutId(): Int = R.layout.fragment_profil

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ProfileSettings()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        color.setOnClickListener(this)
    }
}