package com.trackster.tracksterapp.utils

import ConnectivityUtils
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Broadcast receiver that will handle the network connection change,
 * in case config was not downloaded successfully.
 * Its purpose is to retry downloading the configuration from backend,
 * as soon as Internet connection is available.
 */
class ConnectivityChangeReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(ConnectivityUtils.isConnected(context) && ConfigManager.retryConfigDownload) {
            ConfigManager.getConfig(context)
        }
    }
}