package com.trackster.tracksterapp.chat

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import com.trackster.tracksterapp.BuildConfig
import com.trackster.tracksterapp.model.AdditionalData
import com.trackster.tracksterapp.model.Files
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.rx.RxBus
import com.trackster.tracksterapp.utils.ConfigManager
import com.trackster.tracksterapp.utils.RealPathUtilKotlin
import java.io.File
import com.trackster.tracksterapp.main.MainActivity
import android.os.StrictMode





object DetailsMediaManager {

    const val PICK_IMAGE_REQUEST = 1
    const val REQUEST_IMAGE_CAPTURE = 3
    const val REQUEST_VIDEO_CAPTURE = 4

    private const val JPG_EXT = ".jpg"
    private const val MP4_EXT = ".mp4"
    private const val PROVIDER_EXT = ".provider"
    private const val PDF_EXT = ".pdf"
    const val MESSAGES = "media/messages"

    lateinit var tmpUri: Uri

    var tmpId: Int? = null

    lateinit var fileName: String

    var uploadFile: File? = null

    var uploadsCounter = 0

    var mutableListSendingMessages: MutableList<Message> = mutableListOf()
    var mutableListSendingAudio: MutableList<Files> = mutableListOf()
    var pendingMessage: Message? = null

    fun editUploadsCounter(increase: Boolean, error: Throwable?) {
        if (increase) {
            uploadsCounter++
        } else if (uploadsCounter > 0)
            uploadsCounter--

        RxBus.publish(DetailsMediaUploadEvent(error))
    }

    /**
     * Shows a dialog that enables user to choose a media source.
     */
    fun pickMedia(activity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ) {
            showMediaOptionsDialog(activity)
            true
        } else
            false
    }

    private fun showMediaOptionsDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)

        val pickMediaOptions = arrayOf<CharSequence>(
                "take photo")

        val dialogListener = DialogInterface.OnClickListener { dialogInterface, which ->
            when (which) {
                0 -> dispatchTakePictureIntent(activity)
                1 -> dialogInterface?.cancel()
//                2 -> openImagePicker(activity)
//                3 -> dialogInterface?.cancel()
            }
        }
//
        dialogBuilder.setItems(pickMediaOptions, dialogListener)

        val dialog = dialogBuilder.create()

        val dialogNegativeClickListener = DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel", dialogNegativeClickListener)

        dialog.show()
    }

    private fun dispatchTakePictureIntent(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaUri(JPG_EXT, activity))
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun dispatchTakeVideoIntent(activity: Activity) {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(activity.packageManager) != null) {

            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaUri(MP4_EXT, activity))
            activity.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
        }
    }

    private fun getMediaUri(extension: String, activity: Activity): Uri {
        tmpId = System.currentTimeMillis().toInt()
        fileName = System.currentTimeMillis().toString() + extension
        uploadFile = File(Environment.getExternalStorageDirectory(), fileName)

        // Create the storage directory if it does not exist
        val mediaStorageDir = uploadFile
        if (!mediaStorageDir!!.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("DetailsMediaManager", "failed to create directory")
        }

        uploadFile = File(mediaStorageDir.path + File.separator + fileName)


        tmpUri = getUri(activity)
        return tmpUri
    }
    private fun getMediaFile(extension: String, activity: Activity): File {
        tmpId = System.currentTimeMillis().toInt()
        fileName = System.currentTimeMillis().toString() + extension
        uploadFile = File(Environment.getExternalStorageDirectory(), fileName)

        // Create the storage directory if it does not exist
        val mediaStorageDir = uploadFile
        if (!mediaStorageDir!!.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("DetailsMediaManager", "failed to create directory")
        }

        uploadFile = File(mediaStorageDir.path + File.separator + fileName)


        tmpUri = getUri(activity)
        return uploadFile!!
    }

    private fun getUri(activity: Activity): Uri {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
//        if (Build.VERSION.SDK_INT >= 24)
//            return FileProvider.getUriForFile(
//                activity,
//                BuildConfig.APPLICATION_ID + ".provider",
//                File(Environment.getExternalStorageDirectory(), "image.jpg"))
        return Uri.fromFile(uploadFile)
    }

    private fun openImagePicker(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
//        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
    }

    fun setMediaDataGallery(activity: Activity, uri: Uri) {
        tmpId = System.currentTimeMillis().toInt()
        fileName = System.currentTimeMillis().toString() + JPG_EXT
        uploadFile = File(RealPathUtilKotlin.getRealPath(activity,uri))
        tmpUri = uri
    }

//    fun hasMedia(message: Message) = !TextUtils.isEmpty(message.imageUrl) || !TextUtils.isEmpty(message.videoUrl)

    private fun createUrl(activity: Activity): String = ConfigManager.getAWSCDNUrl(activity) + MESSAGES + File.separator + fileName

    fun createMessage(activity: Activity, content: String?,id : String): Message? {
        return Message(id, content!!,"","","")
    }
    fun createAudio(activity: Activity, document: String): File? {
        return File(document)
    }
    private fun createAdditionalData(hasMedia: Boolean): AdditionalData {
        if (hasMedia) {
            return AdditionalData(tmpId!!, 0, tmpUri.toString(), fileName, uploadFile!!, "", true, false)
        }

        tmpId = System.currentTimeMillis().toInt()
        val id = tmpId

        val additionalData = AdditionalData()
        additionalData.id = id!!

        return additionalData
    }

    private fun getImageLink(activity: Activity, hasMedia: Boolean): String {
        if (hasMedia) {
            val url = createUrl(activity)
            if (url.contains(JPG_EXT)) {
                return url
            }
        }

        return ""
    }

    private fun getVideoLink(activity: Activity, hasMedia: Boolean): String {
        if (hasMedia) {
            val url = createUrl(activity)
            if (url.contains(MP4_EXT)) {
                return url
            }
        }

        return ""
    }

//    fun getPendingMessages(key: Int?): MutableList<Message> {
//        val pendingMessages: MutableList<Message> = mutableListOf()
//        for (pendingMessage in mutableListSendingMessages) {
//            if (pendingMessage.recipient == key) {
//                pendingMessages.add(pendingMessage)
//            }
//        }
//
//        return pendingMessages
//    }

//    fun removeMessage(key: Int?) {
//        val message = mutableListSendingMessages.find { message -> message.additionalData.observerId == key }
//        if (message != null) {
//            mutableListSendingMessages.remove(message)
//        }
//    }
//
//    fun removeMessageId(key: Int?) {
//        val message = mutableListSendingMessages.find { message -> message.additionalData.id == key }
//        if (message != null) {
//            mutableListSendingMessages.remove(message)
//        }
//    }
}