package com.trackster.tracksterapp.base

import android.content.DialogInterface
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.chat.*
import com.trackster.tracksterapp.network.BaseResponse
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.connectivity.NoConnectivityException
import com.trackster.tracksterapp.rx.RxBus
import com.trackster.tracksterapp.utils.ConfigChangedEvent
import com.trackster.tracksterapp.utils.ConfigManager
import com.trackster.tracksterapp.utils.ConnectivityChangeReceiver
import com.trackster.tracksterapp.utils.DialogUtils
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseChatActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null

    lateinit var apiService: PostApi

    private var connectivityChangeReceiver = ConnectivityChangeReceiver()

    private var snackbarUploadInProgress: Snackbar? = null

    private var isActivityVisible = false

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(getLayoutId())

        if (hasToolbar()) {
            setupToolbar()
        }

        initApiService()

        // Listen for media uploads
        setupMediaUploadListeners()
    }

    private fun initApiService() {
        apiService = PostApi.create(this)

        compositeDisposable.add(RxBus.listen(ConfigChangedEvent::class.java).subscribe {
            apiService = PostApi.create(this)
        })

        if (ConfigManager.retryConfigDownload) {
            registerReceiver(connectivityChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun isUpNavigationEnabled(): Boolean

    abstract fun hasToolbar(): Boolean

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(isUpNavigationEnabled())
        supportActionBar?.setHomeButtonEnabled(isUpNavigationEnabled())
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isUpNavigationEnabled()) {
            onBackPressed()
            return true
        }
        return super.onSupportNavigateUp()
    }

    fun setToolbarTitleAndLogo(toolbarTitle: String?, logoUrl: String?) {
        title = toolbarTitle

        toolbar?.setLogo(R.drawable.ic_avatar)
        toolbar?.setTitleMargin(resources.getDimensionPixelSize(R.dimen.toolbar_title_margin_start), 0, 0, 0)

        Glide.with(this)
                .asDrawable()
                .load(logoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(object : SimpleTarget<Drawable>(
                        resources.getDimensionPixelSize(R.dimen.toolbar_logo_width_height),
                        resources.getDimensionPixelSize(R.dimen.toolbar_logo_width_height)) {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        toolbar?.logo = resource
                    }
                })
    }

    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun setupMediaUploadListeners() {
        compositeDisposable.add(RxBus.listen(FeedMediaUploadEvent::class.java).subscribe {

            if (FeedMediaManager.uploadsCounter == 0 && isActivityVisible) {
                if (it.error == null)
                    DialogUtils.showMessage(this@BaseChatActivity, getString(R.string.media_message_sent),
                            DialogInterface.OnDismissListener { dialogInterface: DialogInterface ->
                                RxBus.publish(ClearFeedDataEvent(true))
                                dialogInterface.dismiss()
                            })
                else
                    handleApiError(it.error)
            }
        })

        compositeDisposable.add(RxBus.listen(DetailsMediaUploadEvent::class.java).subscribe {

            if (DetailsMediaManager.uploadsCounter == 0 && isActivityVisible) {
                RxBus.publish(ClearPendingMessageEvent(it.error))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true

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

    override fun onPause() {
        isActivityVisible = false
        super.onPause()
    }

    override fun onDestroy() {
        try {
            unregisterReceiver(connectivityChangeReceiver)
        } catch (exception: IllegalArgumentException) {
            // do nothing
        }
        super.onDestroy()
    }
}
