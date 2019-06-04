package com.trackster.tracksterapp.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.shockwave.pdfium.PdfiumCore
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.MessageRecyclerAdapter
import com.trackster.tracksterapp.base.BaseChatActivity
import com.trackster.tracksterapp.base.TracksterApplication
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.Contact
import com.trackster.tracksterapp.model.Files
import com.trackster.tracksterapp.model.FirebaseMessage
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chat_details.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*


class ChatDetails : BaseChatActivity(), View.OnClickListener {


    // UI components
    private var sendMessageRelativeLayout: LinearLayout? = null
    private var imgSelectorImageView: ImageView? = null
    private var cam: ImageView? = null
    private var sendMessageImageView: ImageView? = null  // mikrofon
    private var microfon: Button? = null
    private var sendMessageEditText: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private lateinit var adapter: MessageRecyclerAdapter
    private var mutableListMessages: MutableList<Message> = mutableListOf()
    private var listMessagesPhotos: ArrayList<String> = arrayListOf()
    var listMessagesCheckSize: MutableList<Message> = mutableListOf()


    private var contact: Contact? = null
    private var isMessageSendable = false
    private var hasMedia = false
    private var isMediaPortrait = false
    private var file: Files? = null

    private lateinit var preloader: RecyclerViewPreloader<Any>

    private var isActivityVisible = false

    // Recording Audio
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null

    var FILE_RECORDING = ""

    val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED
    val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
    val PERMISSION_REQUEST_CODE = 100


    private val compositeDisposable = CompositeDisposable()

    override fun getLayoutId(): Int = R.layout.activity_chat_details

    override fun isUpNavigationEnabled(): Boolean = true

    override fun hasToolbar(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (intent.hasExtra("testPreLoad")) {
            listMessagesPhotos = intent.getStringArrayListExtra("testPreLoad")
        }
        val preloadSizeProvider = ViewPreloadSizeProvider<Any>()
        val modelProvider = MyPreloadModelProvider(listMessagesPhotos, this)
        preloader = RecyclerViewPreloader(Glide.with(this), modelProvider, preloadSizeProvider, 200 /*maxPreload*/)
        initViews()
        getMessages()
        initMutualViews()
        initMessageList()
        FILE_RECORDING = "${externalCacheDir.absolutePath}/recorder.aac"
        setButtonRecordListener()
        setButtonPlayRecordingListener()
        enableDisableButtonPlayRecording()
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true
        registerBroadcastReceivers()

        if (TracksterApplication.shouldReload) {
            getMessages()
        } else {
            TracksterApplication.shouldReload = true
        }
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false
        unregisterBroadcastReceivers()
    }

    private fun registerBroadcastReceivers() {
        registerReceiver(firebaseMessageBroadcastReceiver, IntentFilter(FIREBASE_BROADCAST))
    }

    private fun unregisterBroadcastReceivers() {
        unregisterReceiver(firebaseMessageBroadcastReceiver)
    }

    private fun initMutualViews() {
        sendMessageRelativeLayout = findViewById(R.id.send_message_relative_layout)
        recyclerView = findViewById(R.id.recycler_view_details)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = linearLayoutManager
        adapter = MessageRecyclerAdapter(this, mutableListMessages)
        recyclerView?.adapter = adapter
        recyclerView?.scrollToPosition(adapter.itemCount - 1)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun initMessageList() {
        scrollToBottom()
    }

    private fun setEditTextMaxLength() {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(ConfigManager.getMessageMaxLength(this))
        sendMessageEditText?.filters = filterArray
    }

    private fun initViews() {
        imgSelectorImageView = findViewById(R.id.img_selector_image_view)
        imgSelectorImageView?.setOnClickListener(this)
        sendMessageImageView = findViewById(R.id.button_send_msg)
        sendMessageImageView?.setOnClickListener(this)
        microfon = findViewById(R.id.microfon)
        microfon?.setOnClickListener(this)
        sendMessageEditText = findViewById(R.id.send_message_edit_text)

        cam = findViewById(R.id.cam)
        cam!!.setOnClickListener(this)
        microfon?.visibility = View.VISIBLE
        microfon?.setBackgroundResource(R.drawable.mic)
        sendMessageImageView?.visibility = View.GONE


        sendMessageEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val message = sendMessageEditText?.text
                if (!TextUtils.isEmpty(message)) {
                    isMessageSendable = true
                } else {
                    if (isMessageSendable) {
                        isMessageSendable = false
                    }
                }
                if (message!!.trim().isNotEmpty()) {
                    sendMessageImageView?.visibility = View.VISIBLE
                    microfon?.visibility = View.GONE
                    imgSelectorImageView?.visibility = View.GONE
                    cam?.visibility = View.GONE
                } else {
                    microfon?.visibility = View.VISIBLE
                    sendMessageImageView?.visibility = View.GONE
                    imgSelectorImageView?.visibility = View.VISIBLE
                    cam?.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun scrollToBottom() {
        recyclerView?.scrollToPosition(adapter.itemCount - 1)
    }

    @SuppressLint("LogNotTimber")
    public fun getMessages() {
        apiService = PostApi.create(this@ChatDetails)
        compositeDisposable.add(
            apiService.getChatById(
                PreferenceUtils.getAuthorizationToken(this@ChatDetails),
                PreferenceUtils.getChatId(this@ChatDetails)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.message.size == PreferenceUtils.getSize(this@ChatDetails)) {
                        Log.d("same list", "" + it.message.size + " " + listMessagesCheckSize.size)
                    } else {
                        val difference = (it.message.size - PreferenceUtils.getSize(this@ChatDetails)!!)
                        val list: List<Message> = it.message.takeLast(difference)
                        val iterator = list.listIterator()
                        iterator.forEach { item ->
                            listMessagesCheckSize.add(item)
                            val preffList = PreferenceUtils.getSize(this@ChatDetails)
                            val sumList = (preffList!! + 1)
                            PreferenceUtils.saveMessSize(this@ChatDetails, sumList)
                            if (item.file != null) {
                                if (item.file!!.filename != null) {
                                    doAsync {
                                        getFileFromServer(item.file!!.filename!!)
                                    }.execute()
                                } else {
                                    Log.e("getFileFromServer", "1111")
                                }
                            } else {
                                Log.d("getFileFromServer", "22222")
                            }
                        }
                    }
                    val thread = object : Thread() {
                        override fun run() {
                            try {
                                synchronized(this) {
                                    runOnUiThread {
                                        mutableListMessages = it.message
                                        setData(mutableListMessages)
                                        scrollToBottom()
                                    }
                                }
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    thread.start()
                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                })
        )
    }

    @SuppressLint("LogNotTimber")
    private fun getFileFromServer(filename: String) {
        apiService = PostApi.create(this@ChatDetails)
        compositeDisposable.add(
            apiService.getFileById(
                PreferenceUtils.getAuthorizationToken(this@ChatDetails),
                PreferenceUtils.getChatId(this@ChatDetails), filename
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    doAsync {
                        val writtenToDisk = writeResponseBodyToDisk(it.body(), filename)
                        Log.d("writtenToDisk", "" + writtenToDisk.toString())
                    }.execute()
                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                })
        )
    }

    @SuppressLint("LogNotTimber")
    private fun writeResponseBodyToDisk(body: ResponseBody?, fileName: String): Boolean {
        try {
            val retrofitBetaFile = File(getExternalFilesDir(null).toString() + File.separator + fileName)
            Timber.e(retrofitBetaFile.path)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body?.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body?.byteStream()
                outputStream = FileOutputStream(retrofitBetaFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("writeResponseBodyToDisk", "file download: $fileSizeDownloaded of $fileSize")
                }


                outputStream.flush()

                when {
                    fileName.contains(".pdf") -> {
                        val uri = Uri.fromFile(retrofitBetaFile)
                        generateImageFromPdf(uri)
                    }
                    fileName.contains(".png") -> {
                        val pngString = retrofitBetaFile.toString()
                        val sharedPref =
                            applicationContext.getSharedPreferences(getString(R.string.preff), Context.MODE_PRIVATE)
                        var modelString: MutableList<String?> = mutableListOf()
                        val serializedObject = sharedPref.getString(getString(R.string.sliki), null)
                        if (serializedObject != null) {
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {
                            }.type
                            modelString = gson.fromJson(serializedObject, type)
                        }
                        modelString.add(pngString)
                        val sharedPreferences = getSharedPreferences(getString(R.string.preff), MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(modelString)
                        editor.putString(getString(R.string.sliki), json)
                        editor.apply()
                    }
                    fileName.contains(".aac") || fileName.contains(".mp3") -> {
                        val audioString = retrofitBetaFile.toString()
                        val sharedPref =
                            applicationContext.getSharedPreferences(getString(R.string.preff), Context.MODE_PRIVATE)
                        var modelString: MutableList<String?> = mutableListOf()
                        val serializedObject = sharedPref.getString(getString(R.string.aac), null)
                        if (serializedObject != null) {
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {
                            }.type
                            modelString = gson.fromJson(serializedObject, type)
                        }
                        modelString.add(audioString)
                        val sharedPreferences = getSharedPreferences(getString(R.string.preff), MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(modelString)
                        editor.putString(getString(R.string.aac), json)
                        editor.apply()
                    }
                }

                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }
    }

    public fun generateImageFromPdf(pdfUri: Uri) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this)
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            val fd = contentResolver.openFileDescriptor(pdfUri, "r")
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            saveImage(bmp, pdfUri.toString())
            pdfiumCore.closeDocument(pdfDocument) // important!
        } catch (e: Exception) {
            //todo with exception
        }
    }

    private fun saveImage(bmp: Bitmap, name: String) {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        val folder = Environment.getExternalStorageDirectory().toString() + "/PDF"
        var out: FileOutputStream? = null
        try {
            val newFolder = File(folder)
            if (!newFolder.exists())
                newFolder.mkdirs()
            val domain: String? = name.substringAfterLast("/")
            val file = File(newFolder, "$domain.png")
            val sharedPref = applicationContext.getSharedPreferences(getString(R.string.preff), Context.MODE_PRIVATE)
            var modelString: MutableList<String?> = mutableListOf()
            val serializedObject = sharedPref.getString(getString(R.string.sliki), null)
            if (serializedObject != null) {
                val gson = Gson()
                val type = object : TypeToken<List<String>>() {
                }.type
                modelString = gson.fromJson(serializedObject, type)
            }
            modelString.add(file.toString())
            val sharedPreferences = getSharedPreferences(getString(R.string.preff), MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(modelString)
            editor.putString(getString(R.string.sliki), json)
            editor.apply()
            out = FileOutputStream(file)

            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val inputStream = FileInputStream(file)

            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)

            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // bmp is your Bitmap instance
        } catch (e: Exception) {
            //todo with exception
        } finally {
            try {
                out?.close()
            } catch (e: Exception) {
                //todo with exception
            }
        }
    }

    private fun setData(result: MutableList<Message>) {
        adapter.setData(result)
        adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_send_msg -> {
                sendMessage(
                    DetailsMediaManager.createMessage(
                        this, sendMessageEditText?.text.toString(),
                        PreferenceUtils.getUserId(this@ChatDetails),
                        PreferenceUtils.getUserId(this@ChatDetails),
                        PreferenceUtils.getUserId(this@ChatDetails),
                        "123", file
                    )!!
                )
                clearMessage()
            }
            R.id.microfon -> {
                startRecording()
            }
            // R.id.img_selector_image_view -> addMedia()

            R.id.cam -> addMedia()

        }

    }

    fun startRecording() {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (grantResults[0] == PERMISSION_GRANTED) {
                    record()
                }
            }
        }

    }

    private fun enableDisableButtonPlayRecording() {
        buttonPlayRecording.isEnabled = doesFileExist()
    }

    private fun doesFileExist(): Boolean {
        val file = File(FILE_RECORDING)
        return file.exists()
    }

    private fun setButtonRecordListener() {
        microfon?.setOnClickListener {
            if (microfon?.text.toString().equals(getString(R.string.record), true)) {
                record()
            } else {
                stopRecording()
                enableDisableButtonPlayRecording()
                microfon?.text = getString(R.string.record)
                microfon?.setBackgroundResource(R.drawable.mic)
                sendAudio()
            }
        }
    }

    private fun setButtonPlayRecordingListener() {
        buttonPlayRecording.setOnClickListener {
            if (buttonPlayRecording.text.toString().equals(getString(R.string.playRecord), true)) {
                buttonPlayRecording.text = getString(R.string.stopPlayingRecord)
                playRecording()
            } else {
                buttonPlayRecording.text = getString(R.string.playRecord)
                stopPlayingRecording()
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkSelfPermission(AUDIO_PERMISSION) == PERMISSION_GRANTED
        else return true

    }

    private fun requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(AUDIO_PERMISSION), PERMISSION_REQUEST_CODE)
        }
    }

    private fun record() {
        if (!isPermissionGranted()) {
            requestAudioPermission()
            return
        }

        microfon?.setBackgroundResource(R.drawable.stop)
        microfon?.text = getString(R.string.stopRecording)
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
        mediaRecorder!!.setOutputFile(FILE_RECORDING)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.prepare()
        mediaRecorder!!.start()
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private fun playRecording() {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(FILE_RECORDING)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener {
            buttonPlayRecording.text = getString(R.string.playRecord)
        }
    }

    private fun stopPlayingRecording() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    @SuppressLint("LogNotTimber")
    private fun postAudio() {
        val file = File(FILE_RECORDING)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData(getString(R.string.document), file.name, requestBody)
        apiService = PostApi.create(this@ChatDetails)
        compositeDisposable.add(
            apiService.postAudio(
                PreferenceUtils.getAuthorizationToken(this@ChatDetails),
                PreferenceUtils.getChatId(this@ChatDetails), body
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    doAsync {
                        getMessages()
                    }.execute()
                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                    //                showProgress(false)
                })
        )
    }

    @SuppressLint("LogNotTimber")
    private fun postPDF(file: File) {
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData(getString(R.string.document), file.name, requestBody)
        apiService = PostApi.create(this@ChatDetails)
        compositeDisposable.add(
            apiService.postAudio(
                PreferenceUtils.getAuthorizationToken(this@ChatDetails),
                PreferenceUtils.getChatId(this@ChatDetails), body
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    doAsync {
                        getMessages()
                    }.execute()
                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                })
        )
    }

    private fun setAudioFile(file: Files) {

        // var files = FILE_RECORDING
        file.filename = FILE_RECORDING
    }

    private fun clearMessage() {
        sendMessageEditText?.setText("")
        hasMedia = false
    }

    private fun sendAudio() {
        if (hasMedia) {
            hasMedia = false
        } else {
            doAsync {
                postAudio()
            }.execute()
        }
        scrollToBottom()
    }

    private fun sendMessage(message: Message) {
        if (hasMedia) {
            hasMedia = false
        } else {
            postMessage(message)
        }
        adapter.addMessage(message)
        scrollToBottom()
    }

    @SuppressLint("LogNotTimber")
    private fun postMessage(message: Message) {
        apiService = PostApi.create(this@ChatDetails)
        compositeDisposable.add(
            apiService.postMessage(
                PreferenceUtils.getAuthorizationToken(this@ChatDetails),
                PreferenceUtils.getChatId(this@ChatDetails), message
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mutableListMessages.add(it)
                    adapter.setData(createNewData())
                    scrollToBottom()
                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                })
        )
    }

    private fun createNewData(): MutableList<Message> =
        mutableListMessages.asSequence().toMutableList()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty()) {
                when (grantResults[0]) {
                    0 -> addMedia()
                }
            }
        }
    }

    private fun addMedia() {
        if (!DetailsMediaManager.pickMedia(this))
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        hasMedia = false
        if (requestCode == DetailsMediaManager.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            DetailsMediaManager.setMediaDataGallery(this, data?.data!!)
            setMedia(data.data)
        } else if ((requestCode == DetailsMediaManager.REQUEST_IMAGE_CAPTURE || requestCode == DetailsMediaManager.REQUEST_VIDEO_CAPTURE)
            && resultCode == Activity.RESULT_OK
        ) {
            probaPdf(DetailsMediaManager.tmpUri)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ChatDetails, MainScreenActivity::class.java))
    }

    private fun probaPdf(uri: Uri?): File {
        val document = Document()
        val f = File(Environment.getExternalStorageDirectory(), "PDF_Images.pdf")
        PdfWriter.getInstance(document, FileOutputStream(f)) // Change pdf's name.
        document.open()
        val image = Image.getInstance(uri?.path!!) // Change image's name and extension.
        val scaler = (((((document.pageSize.width - document.leftMargin()
                - document.rightMargin() - 0)) / image.width)) * 100) // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler)
        image.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
        document.add(image)
        document.close()
        doAsync {
            postPDF(f)
        }.execute()

        return f
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }

    private fun setMedia(uri: Uri?) {
        val exif = ExifInterface(DetailsMediaManager.uploadFile?.absolutePath)
        val orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)

        isMediaPortrait = false

        isMediaPortrait = when (orientation.toInt()) {
            6 -> {
                true
            }
            8 -> {
                true
            }
            else -> {
                false
            }
        }

        Glide.with(this).load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(imgSelectorImageView!!)

        sendMessageImageView?.setImageResource(R.drawable.arrow_send_blue)
        isMessageSendable = true
        hasMedia = true
    }

    private var firebaseMessageBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val firebaseMessage = intent.getSerializableExtra(FIREBASE_MESSAGE_KEY) as FirebaseMessage?
//            if (firebaseMessage?.contactID == contact?.id) {
            getMessages()
//            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
