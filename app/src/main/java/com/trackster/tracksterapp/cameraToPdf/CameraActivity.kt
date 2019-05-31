package com.trackster.tracksterapp.cameraToPdf

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.content_camera.*
import java.io.File
import java.io.FileOutputStream


class CameraActivity : AppCompatActivity() {

    var fotoapparat: Fotoapparat? = null
    var fotoapparatState: FotoapparatState? = null
    var cameraStatus: CameraState? = null
    var flashState: FlashState? = null
    val model: MutableList<File> = mutableListOf()
    var tmpId: Int? = null
    private val JPG_EXT = ".jpg"
    val REQUEST_IMAGE_CAPTURE = 3
    lateinit var fileName: String
    var uploadFile: File? = null
    lateinit var tmpUri: Uri
    val modelPDF: MutableList<Document> = mutableListOf()
    val modelString: MutableList<String?> = mutableListOf()

    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.trackster.tracksterapp.R.layout.activity_camera)
        createFotoapparat()
        cameraStatus = CameraState.BACK
        flashState = FlashState.OFF
        fotoapparatState = FotoapparatState.OFF

        fab_camera.setOnClickListener {
            takePhoto()
        }

        fab_switch_camera.setOnClickListener {
            //            switchCamera()
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            val uris = ArrayList<Uri>()
            //convert from paths to Android friendly Parcelable Uri's
            for (file in modelString) {
                val fileIn = File(file)
                val u = Uri.fromFile(fileIn)
                uris.add(u)
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            intent.type = "application/pdf"
            startActivity(Intent.createChooser(intent, "Send"))
        }
        fab_flash.setBackgroundResource(R.drawable.flash1)
        fab_flash.setOnClickListener {
                changeFlashState()
        }
    }

    private fun createFotoapparat() {
        val cameraView = findViewById<CameraView>(com.trackster.tracksterapp.R.id.camera_view)

        fotoapparat = Fotoapparat(
            context = this,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(
                logcat()
            ),
            cameraErrorCallback = { error ->
                println("Recorder errors: $error")
            }
        )
    }

    private fun changeFlashState() {
        fotoapparat?.updateConfiguration(
            CameraConfiguration(
                flashMode = if (flashState == FlashState.TORCH) off() else torch()
            )
        )

        flashState = when (flashState) {
            FlashState.TORCH -> FlashState.OFF
            else -> FlashState.TORCH
        }
    }

    private fun takePhoto() {
        val tmpId = System.currentTimeMillis().toString()
        val filename = "$tmpId.jpg"
        val sd = android.os.Environment.getExternalStorageDirectory()
        val dest = File(sd, filename)
        if (hasNoPermissions()) {
            requestPermission()
        } else {
            fotoapparat
                ?.takePicture()
                ?.saveToFile(dest)
            createFotoapparat()
            dispatchTakePictureIntent(this@CameraActivity)
        }
    }

    private fun dispatchTakePictureIntent(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaUri(JPG_EXT, activity))
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == REQUEST_IMAGE_CAPTURE)
            && resultCode == Activity.RESULT_OK
        ) {
            probaPdf(tmpUri)
        }
    }

    private fun probaPdf(uri: Uri?): File {
        val tmpId = System.currentTimeMillis().toString()
        val document = Document()
        val f = File(Environment.getExternalStorageDirectory(), "$tmpId.pdf")
        val directoryPath = f.toString()
        PdfWriter.getInstance(document, FileOutputStream(f)) // Change pdf's name.
        document.open()
        val image = Image.getInstance(uri?.path!!) // Change image's name and extension.
        val scaler = (((((document.pageSize.width - document.leftMargin()
                - document.rightMargin() - 0)) / image.width)) * 100) // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler)
        image.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
        document.add(image)
        document.close()
        modelPDF.add(document)
        modelString.add(directoryPath)
        return f
    }

    override fun onStart() {
        super.onStart()
        if (hasNoPermissions()) {
            requestPermission()
        } else {
            fotoapparat?.start()
            fotoapparatState = FotoapparatState.ON
        }
    }

    private fun hasNoPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 0)
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

    override fun onResume() {
        super.onResume()
        if (!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF) {
            val intent = Intent(baseContext, CameraActivity::class.java)
            startActivity(intent)
            finish()
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

    override fun onBackPressed() {
        startActivity(Intent(this@CameraActivity, MainScreenActivity::class.java))
    }
}

enum class CameraState {
    FRONT, BACK
}

enum class FlashState {
    TORCH, OFF
}

enum class FotoapparatState {
    ON, OFF
}


