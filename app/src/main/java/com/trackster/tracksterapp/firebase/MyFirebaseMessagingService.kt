package com.trackster.tracksterapp.firebase

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.AsyncTask
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.chat.ChatDetails
import com.trackster.tracksterapp.model.FirebaseMessage
import com.trackster.tracksterapp.utils.*
import java.util.*
import java.util.concurrent.ExecutionException

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FirebaseMessagingService"
    private var pendingIntent: PendingIntent? = null

    private lateinit var intent: Intent
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val id = remoteMessage.data.get(FIREBASE_MESSAGE_PAYLOAD_ID_KEY)
        val message = remoteMessage?.data?.get(FIREBASE_MESSAGE_PAYLOAD_KEY)
        val contactName = remoteMessage?.data?.get(FIREBASE_CONTACT_NAME_PAYLOAD_KEY)
        val contactId = remoteMessage?.data?.get(FIREBASE_CONTACT_ID_PAYLOAD_KEY)
        val createTime = remoteMessage.data.get(FIREBASE_PUSH_NOTIFICATION_ID_PAYLOAD_KEY)
        sendBroadcastMessage(FirebaseMessage(id!!, message!!, contactName!!, contactId!!, createTime!!))
        if (remoteMessage.data != null) {
            showNotif()
        }
    }

    private fun showNotif() {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendBroadcastMessage(firebaseMessage: FirebaseMessage) {
        val intent = Intent(FIREBASE_BROADCAST)
        intent.putExtra(FIREBASE_MESSAGE_KEY, firebaseMessage)
        sendBroadcast(intent)
    }

    private fun showNotification(title: String?, body: String?) {
//        val intent = Intent(this, MainScreenActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT)
        var foreground = false
        val intent: Intent
        val random = Random().nextInt(1000).toString()
        try {
            foreground = ForegroundCheckTask().execute(this).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        if (foreground) { //app in foreground
            intent = Intent(this, ChatDetails::class.java)
            intent.putExtra(CONTENT_KEY, 1)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            pendingIntent = PendingIntent.getActivity(
                this,
                Integer.valueOf(random) /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            startActivity(intent)      // to directly open activity if app is foreground
        } else { //app in background
            intent = Intent(this, ChatDetails::class.java)
            intent.putExtra(CONTENT_KEY, 1)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            pendingIntent = PendingIntent.getActivity(
                this,
                Integer.valueOf(random) /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    @SuppressLint("StaticFieldLeak")
    private inner class ForegroundCheckTask : AsyncTask<Context, Void, Boolean>() {

        override fun doInBackground(vararg params: Context): Boolean? {
            val context = params[0].applicationContext
            return isAppOnForeground(context)
        }

        private fun isAppOnForeground(context: Context): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appProcesses = activityManager.runningAppProcesses ?: return false
            val packageName = context.packageName
            for (appProcess in appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                    return true
                }
            }
            return false
        }
    }
}