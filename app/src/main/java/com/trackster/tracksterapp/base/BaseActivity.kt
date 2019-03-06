package com.trackster.tracksterapp.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.BaseResponse
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.connectivity.NoConnectivityException
import com.trackster.tracksterapp.utils.DialogUtils
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseActivity<P : BasePresenter<BaseView>> : BaseView, AppCompatActivity() {

    lateinit var apiService: PostApi

    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initApiService()
        presenter = instantiatePresenter()
    }

    /**
     * Instantiates the presenter the Activity is based on.
     */
    protected abstract fun instantiatePresenter(): P

    abstract fun getLayoutId(): Int

    override fun getContext(): Context {
        return this
    }
    private fun initApiService() {
        apiService = PostApi.create(this)


    }
    fun handleApiError(error: Throwable?) {
        when (error) {
            is HttpException -> {
                try {
                    val response = Gson().fromJson(error.response().errorBody()?.string(), BaseResponse::class.java)
                    val errorMessage = response?.errorMessages?.get(0)
                    if (TextUtils.isEmpty(errorMessage)) {
                        DialogUtils.showGeneralErrorMessage(this)
                    } else {
                        DialogUtils.showErrorMessage(this, errorMessage)
                    }
                } catch (exception: JsonSyntaxException) {
                    DialogUtils.showGeneralErrorMessage(this)
                }
            }
            is NoConnectivityException -> {
                DialogUtils.showErrorMessage(this, getString(R.string.general_error_no_internet))
            }
            is SocketTimeoutException -> {
                DialogUtils.showGeneralErrorMessage(this)
            }
            else -> {
                DialogUtils.showGeneralErrorMessage(this)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        (supportFragmentManager.fragments.last() as BaseMainViewFragment)?.onActivityResult(requestCode, resultCode, data)
    }

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            BaseMainActivity.PERMISSION.MY_PERMISSION_CAMERA)
    }
}