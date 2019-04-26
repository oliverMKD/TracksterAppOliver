package com.trackster.tracksterapp.chat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import com.trackster.tracksterapp.BuildConfig
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.rx.RxBus
import com.trackster.tracksterapp.utils.ConfigManager
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.RealPath
import com.trackster.tracksterapp.utils.RealPathUtilKotlin
import java.io.File

object FeedMediaManager {

    const val PICK_IMAGE_REQUEST = 1
    const val REQUEST_IMAGE_CAPTURE = 3
    const val REQUEST_VIDEO_CAPTURE = 4

    private const val JPG_EXT = ".jpg"
    private const val MP4_EXT = ".mp4"
    private const val PROVIDER_EXT = ".provider"

    const val SOCIAL = "media/social"

    lateinit var tmpUri: Uri

    lateinit var fileName: String

    var uploadFile: File? = null

    var uploadsCounter = 0
    var feedDescription = ""

    fun editUploadsCounter(increase: Boolean, error: Throwable?) {
        if (increase) {
            uploadsCounter++
        } else if (uploadsCounter > 0)
            uploadsCounter--

        RxBus.publish(FeedMediaUploadEvent(error))
    }

    fun clearUploadData() {
        uploadFile = null
        feedDescription = ""
    }

    fun retrieveDataFromPreferences(context: Context, savedMediaUrl: String) {
//        feedDescription = PreferenceUtils.getFeedDescription(context)
        tmpUri = Uri.parse(savedMediaUrl)
//        fileName = PreferenceUtils.getFeedMediaFileName(context)
//        uploadFile = File(PreferenceUtils.getFeedMediaFilePath(context))
    }

    /**
     * Shows a dialog that enables user to choose a media source.
     */
    fun pickMedia(activity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showMediaOptionsDialog(activity)
            true
        } else
            false
    }

    private fun showMediaOptionsDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)

        val pickMediaOptions = arrayOf<CharSequence>(activity.getString(R.string.make_video),
                activity.getString(R.string.take_photo), activity.getString(R.string.upload_from_library))

        val dialogListener = DialogInterface.OnClickListener { dialogInterface, which ->
            when (which) {
                0 -> dispatchTakeVideoIntent(activity)
                1 -> dispatchTakePictureIntent(activity)
                2 -> openImagePicker(activity)
                3 -> dialogInterface?.cancel()
            }
        }
        dialogBuilder.setItems(pickMediaOptions, dialogListener)

        val dialog = dialogBuilder.create()

        val dialogNegativeButtonListener = DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getString(R.string.cancel), dialogNegativeButtonListener)

        dialog.show()
    }

    private fun dispatchTakePictureIntent(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaUri(JPG_EXT, activity))
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
        fileName = System.currentTimeMillis().toString() + extension
        uploadFile = File(Environment.getExternalStorageDirectory(), fileName)
        tmpUri = getUri(activity)
        return tmpUri
    }

    private fun getUri(activity: Activity): Uri {
        if (Build.VERSION.SDK_INT >= 24)
            return FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + PROVIDER_EXT, uploadFile!!)
        return Uri.fromFile(uploadFile)
    }

    private fun openImagePicker(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
    }

    fun setMediaDataGallery(activity: Activity, uri: Uri) {
        fileName = System.currentTimeMillis().toString() + JPG_EXT
        uploadFile = File(RealPathUtilKotlin.getRealPath(activity, uri))
        tmpUri = uri
    }

    fun setMediaDataShareImage(activity: Activity, uri: Uri) {
        fileName = System.currentTimeMillis().toString() + JPG_EXT
        uploadFile = File(RealPath.getRealPathFromURICapturedImage(activity, uri))
        tmpUri = uri
    }

    private fun setMediaDataShareVideo(activity: Activity, uri: Uri) {
        fileName = System.currentTimeMillis().toString() + MP4_EXT
        uploadFile = File(RealPath.getRealPathFromURICapturedVideo(activity, uri))
        tmpUri = getUri(activity)
    }

    fun isVideoValid(activity: Activity, videoUri: Uri): Boolean {


        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(activity, tmpUri)

        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()

        val timeInMillis = time.toLongOrNull() ?: 0
        return timeInMillis / 1000 <= ConfigManager.getVideoMaxLength(activity)
    }

    fun createAWSUrl(activity: Activity): String = ConfigManager.getAWSCDNUrl(activity) + SOCIAL + File.separator + fileName
}