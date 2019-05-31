package com.trackster.tracksterapp.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import com.trackster.tracksterapp.model.Files
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.utils.RealPathUtilKotlin
import java.io.File


object DetailsMediaManager {

    const val PICK_IMAGE_REQUEST = 1
    const val REQUEST_IMAGE_CAPTURE = 3
    const val REQUEST_VIDEO_CAPTURE = 4

    private const val JPG_EXT = ".jpg"
    lateinit var tmpUri: Uri
    var tmpId: Int? = null
    lateinit var fileName: String
    var uploadFile: File? = null
    var uploadsCounter = 0

    /**
     * Shows a dialog that enables user to choose a media source.
     */
    fun pickMedia(activity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            showMediaOptionsDialog(activity)
            true
        } else
            false
    }

    private fun showMediaOptionsDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)

        val pickMediaOptions = arrayOf<CharSequence>(
            "take photo"
        )
        val dialogListener = DialogInterface.OnClickListener { dialogInterface, which ->
            when (which) {
                0 -> dispatchTakePictureIntent(activity)
                1 -> dialogInterface?.cancel()
            }
        }
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
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @SuppressLint("LogNotTimber")
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

    private fun getUri(activity: Activity): Uri {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return Uri.fromFile(uploadFile)
    }

    fun setMediaDataGallery(activity: Activity, uri: Uri) {
        tmpId = System.currentTimeMillis().toInt()
        fileName = System.currentTimeMillis().toString() + JPG_EXT
        uploadFile = File(RealPathUtilKotlin.getRealPath(activity, uri))
        tmpUri = uri
    }

    fun createMessage(
        activity: Activity,
        content: String?,
        createdBy: String?,
        senderId: String?,
        createdTime: String?,
        id: String,
        file: Files?
    ): Message? {
        return Message(id, content!!, createdBy!!, senderId!!, createdTime!!, file)
    }
}