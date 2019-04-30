package com.trackster.tracksterapp.base

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.trackster.tracksterapp.utils.ConfigManager

class TracksterApplication : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        private var instance: TracksterApplication? = null

        var shouldReload = true
    }

    override fun onCreate() {
        super.onCreate()

        ConfigManager.getConfig(applicationContext)
    }

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }
}