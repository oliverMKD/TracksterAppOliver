package com.trackster.tracksterapp.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.AmazonApiService
import com.trackster.tracksterapp.rx.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

object ConfigManager {

    private var config: Config? = null

    var retryConfigDownload = false

    fun getConfig(context: Context) {
        val amazonApiService = AmazonApiService.create(context)
        CompositeDisposable().add(amazonApiService.getConfig()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    config = result
                    Log.d("TVChat", "config " + result.toString())
//                    PreferenceUtils.saveBaseUrl(context, result.BASE_URL)
                    retryConfigDownload = false
                    RxBus.publish(ConfigChangedEvent(retryConfigDownload))
                }, { _ ->
                    retryConfigDownload = true
                }))
    }

    fun getAWSCDNUrl(context: Context): String? =
            if (TextUtils.isEmpty(config?.AWS_CDN_URL)) context.getString(R.string.aws_cdn_default)
            else config?.AWS_CDN_URL

    fun getAWSUrl(context: Context): String? =
            if (TextUtils.isEmpty(config?.AWS_MAIN_URL)) context.getString(R.string.aws_url_default)
            else config?.AWS_MAIN_URL

    fun getAWSAccessKey(context: Context): String? =
            if (TextUtils.isEmpty(config?.AWS_ACCESS_KEY)) context.getString(R.string.aws_access_key_default)
            else config?.AWS_ACCESS_KEY

    fun getAWSSecretKey(context: Context): String? =
            if (TextUtils.isEmpty(config?.AWS_ACCESS_SECRET)) context.getString(R.string.aws_secret_key_default)
            else config?.AWS_ACCESS_SECRET

    fun getAWSBucketName(context: Context): String? =
            if (TextUtils.isEmpty(config?.AWS_BUCKET_NAME)) context.getString(R.string.aws_bucket_name_default)
            else config?.AWS_BUCKET_NAME

    fun getVideoMaxLength(context: Context): Int =
            if (config == null || config?.VIDEO_MAX_LENGTH!! <= 0) context.resources.getInteger(R.integer.video_max_length_default)
            else config?.VIDEO_MAX_LENGTH!!

    fun getMessageMaxLength(context: Context): Int =
            if (config == null || config?.MESSAGE_MAX_LENGTH!! <= 0) context.resources.getInteger(R.integer.message_max_length_default)
            else config?.MESSAGE_MAX_LENGTH!!
}