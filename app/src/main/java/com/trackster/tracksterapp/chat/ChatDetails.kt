package com.trackster.tracksterapp.chat

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
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
import android.provider.DocumentsContract
import android.provider.MediaStore
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
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
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
import com.trackster.tracksterapp.firebase.FirebaseUtils
import com.trackster.tracksterapp.main.MainActivity
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
import kotlinx.coroutines.android.awaitFrame
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*


class ChatDetails() : BaseChatActivity(), View.OnClickListener {

    object Day {
        const val TODAY = "today"
        const val YESTERDAY = "yesterday"
    }

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
    private var audioListMessages: MutableList<Files> = mutableListOf()
    private var listMessagesPhotos: ArrayList<String> = arrayListOf()
    var listMessagesCheckSize: MutableList<Message> = mutableListOf()


    private var files: Files? = null
    private var contact: Contact? = null
    private var sender: Contact? = null
    private var recipient: Contact? = null
    private var previousDate: String = ""
    private var isMessageSendable = false
    private var hasMedia = false
    private var isMediaPortrait = false
    private var observerId: Int? = null
    lateinit var apkStorage: File
    private var file: Files? = null
    private var createdBy: String? = null
    private var senderId: String? = null
    private var createdTime: String? = null

    private lateinit var preloader: RecyclerViewPreloader<Any>

    private var isActivityVisible = false

    // Recording Audio
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null

    var FILE_RECORDING = ""

    val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED
    val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
    val PERMISSION_REQUEST_CODE = 100


    // aws
    private var s3Client: AmazonS3Client? = null

    private val compositeDisposable = CompositeDisposable()
//    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

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
        registerReceiver(broadcastReceiver, IntentFilter(TRY_AGAIN_BROADCAST))
        registerReceiver(firebaseMessageBroadcastReceiver, IntentFilter(FIREBASE_BROADCAST))
    }

    private fun unregisterBroadcastReceivers() {
        unregisterReceiver(broadcastReceiver)
        unregisterReceiver(firebaseMessageBroadcastReceiver)
    }

    private fun listenForUploadReady() {
//        compositeDisposable.add(RxBus.listen(UploadReadyEvent::class.java).subscribe {
//            if (it.message != null) {
//                DetailsMediaManager.mutableListSendingMessages.remove(it.message)
//                if (isActivityVisible && it.message.recipient == contact?.id) {
//                    adapter.removeMessage(it.message.additionalData.id)
//                    DetailsMediaManager.mutableListSendingMessages.remove(it.message)
//                    setDateForMessage(it.message)
//                    mutableListMessages.add(it.message)
//
//                    adapter.setData(createNewData())
//                    scrollToBottom()
//                }
//            }
//        })
    }

    private fun setPaddingSendMessageRelativeLayout(isSnackbarVisible: Boolean) {
        val sendMessageViewPadding = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin)

        if (isSnackbarVisible) {
            sendMessageRelativeLayout?.setPadding(
                sendMessageViewPadding,
                sendMessageViewPadding,
                sendMessageViewPadding,
                resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin)
            )
        } else {
            sendMessageRelativeLayout?.setPadding(
                sendMessageViewPadding,
                sendMessageViewPadding,
                sendMessageViewPadding,
                sendMessageViewPadding
            )
        }
    }

    private fun initMutualViews() {
        sendMessageRelativeLayout = findViewById(R.id.send_message_relative_layout)
        recyclerView = findViewById(R.id.recycler_view_details)
        val linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        recyclerView?.layoutManager = linearLayoutManager
        adapter = MessageRecyclerAdapter(this, mutableListMessages)
        recyclerView?.adapter = adapter
        recyclerView?.scrollToPosition(adapter.itemCount - 1)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun initAWS() {
        val s3Client = AmazonS3Client(
            BasicAWSCredentials(
                ConfigManager.getAWSAccessKey(this),
                ConfigManager.getAWSSecretKey(this)
            ), Utils.getAWSConfigurationClient()
        )
        this.s3Client = s3Client
//        transferUtility = TransferUtility(s3Client, applicationContext)
    }

    private fun handleIntent() {
        contact = intent.getSerializableExtra(CONTACT_KEY) as Contact?
        setToolbarTitleAndLogo(contact?.nickname, contact?.avatar)
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
//        sendMessageEditText?.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {   sendMessageImageView?.setImageResource(R.drawable.spajalica)}
//        }

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

                if (message!!.trim().length > 0) {
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

//    fun openMediaDetails(intent: Intent) {
//        if (isMotherphone) {
//            intent.putExtra(Constants.SENDER_KEY, sender)
//            intent.putExtra(Constants.RECIPIENT_KEY, recipient)
//        } else {
//            intent.putExtra(CONTACT_KEY, contact)
//        }
//
//        startActivity(intent)
//    }

    private fun scrollToBottom() {
        recyclerView?.scrollToPosition(adapter.itemCount - 1)
    }


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
                        var aa = (it.message.size - PreferenceUtils.getSize(this@ChatDetails)!!)
                        var list: List<Message> = it.message.takeLast(aa)
                        val iterator = list.listIterator()
                        for (item in iterator) {
                            listMessagesCheckSize.add(item)
                            var preffList = PreferenceUtils.getSize(this@ChatDetails)
                            var sumList = (preffList!! + 1)
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
                    //                showProgress(false)
                })
        )
    }

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

    private fun writeResponseBodyToDisk(body: ResponseBody?, fileName: String): Boolean {
        try {
// todo change the file location/name according to your needs

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
                    outputStream!!.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("writeResponseBodyToDisk", "file download: $fileSizeDownloaded of $fileSize")
                }


                outputStream!!.flush()

                when {
                    fileName.contains(".pdf") -> {
                        val uri = Uri.fromFile(retrofitBetaFile)
                        generateImageFromPdf(uri)
                    }
                    fileName.contains(".png") -> {
                        val pngString = retrofitBetaFile.toString()
                        val sharedPref = applicationContext.getSharedPreferences("preff", Context.MODE_PRIVATE)
                        var modelString: MutableList<String?> = mutableListOf()
                        val serializedObject = sharedPref.getString("sliki", null)
                        if (serializedObject != null) {
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {
                            }.type
                            modelString = gson.fromJson(serializedObject, type)
                        }
                        modelString.add(pngString)
                        val sharedPreferences = getSharedPreferences("preff", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(modelString)
                        editor.putString("sliki", json)
                        editor.apply()
                    }
                    fileName.contains(".aac") || fileName.contains(".mp3") -> {
                        val audioString = retrofitBetaFile.toString()
                        val sharedPref = applicationContext.getSharedPreferences("preff", Context.MODE_PRIVATE)
                        var modelString: MutableList<String?> = mutableListOf()
                        val serializedObject = sharedPref.getString("aac", null)
                        if (serializedObject != null) {
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {
                            }.type
                            modelString = gson.fromJson(serializedObject, type)
                        }
                        modelString.add(audioString)
                        val sharedPreferences = getSharedPreferences("preff", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(modelString)
                        editor.putString("aac", json)
                        editor.apply()
                    }
                }

                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream!!.close()
                }

                if (outputStream != null) {
                    outputStream!!.close()
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
        var o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        val FOLDER = Environment.getExternalStorageDirectory().toString() + "/PDF"
        var out: FileOutputStream? = null
        try {
            val folder = File(FOLDER)
            if (!folder.exists())
                folder.mkdirs()
            val domain: String? = name.substringAfterLast("/")
            val file = File(folder, "$domain.png")
            val sharedPref = applicationContext.getSharedPreferences("preff", Context.MODE_PRIVATE)
            var modelString: MutableList<String?> = mutableListOf()
            val serializedObject = sharedPref.getString("sliki", null)
            if (serializedObject != null) {
                val gson = Gson()
                val type = object : TypeToken<List<String>>() {
                }.type
                modelString = gson.fromJson(serializedObject, type)
            }
            modelString.add(file.toString())
            val sharedPreferences = getSharedPreferences("preff", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(modelString)
            editor.putString("sliki", json)
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

            var o2 = BitmapFactory.Options()
            o2.inSampleSize = scale;
            var inputStream = FileInputStream(file)

            var selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            var outputStream = FileOutputStream(file)

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
//        result.reverse()
//        setDatesData(result)
        adapter.setData(result)
        adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    private fun setDatesData(list: MutableList<Message>) {
        for (message: Message in list) {
            setDateForMessage(message)
        }
    }

    private fun setDateForMessage(message: Message) {
        // val additionalData = DateFormat.formatDate(message.createTime, DateFormat.DATE_FORMAT_MESSAGE_DETAILS)
//
//        var messageDate = DateFormat.formatDate(message.createTime, DateFormat.DATE_FORMAT_MESSAGE_DETAILS)
//        messageDate = DateFormat.formatDateDetailsMessage(message.createTime, messageDate)
//        if (previousDate == messageDate) {
//            message.createTime = ""
//        } else {
//            message.createTime = messageDate
//            previousDate = messageDate
//        }

        ///  var nova_data : String = ""
        //nova_data = DateFormat.formatDate(additionalData,DateFormat.DATE_FORMAT_MESSAGE_DETAILS)
        // LocalDateTime.parse(nova_data, ISODateTimeFormat.dateTimeParser())


        //  message.createTime = nova_data


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
            R.id.img_selector_image_view -> addMedia()

            // R.id.cam ->
            // authenticateWithFB()
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

    private fun postAudio() {
        val file = File(FILE_RECORDING)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
// MultipartBody.Part is used to send also the actual file name
        val body =
            MultipartBody.Part.createFormData("document", file.name, requestBody)
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


    private fun postPDF(file: File) {
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
// MultipartBody.Part is used to send also the actual file name
        val body =
            MultipartBody.Part.createFormData("document", file.name, requestBody)
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

    private fun createAudioData(): MutableList<Files> =
        audioListMessages.asSequence().toMutableList()


    private fun clearMessage() {
        sendMessageEditText?.setText("")
        hasMedia = false
        removeMedia()
        //  sendMessageImageView?.setImageResource(R.drawable.spajalica)
    }


    private fun sendAudio() {
        if (hasMedia) {
            hasMedia = false

            DetailsMediaManager.editUploadsCounter(true, null)
//           uploadAudio(file)

        } else {
            doAsync {
                postAudio()
            }.execute()
        }

//        DetailsMediaManager.mutableListSendingAudio.add(file)
//        adapter.addAudio(file)
        scrollToBottom()
    }

    private fun sendMessage(message: Message) {
        if (hasMedia) {
            hasMedia = false

            DetailsMediaManager.editUploadsCounter(true, null)
            uploadMedia(message)

        } else {
            postMessage(message)
        }

//        DetailsMediaManager.mutableListSendingMessages.add(message)
        adapter.addMessage(message)
        scrollToBottom()
    }


//    private fun messageNotSent(message: Message) {
//        adapter.removeMessage(message.additionalData.id)
//        DetailsMediaManager.removeMessage(message.additionalData.id)
//        message.additionalData.isSending = false
//        message.additionalData.errorSending = true
//        mutableListMessages.add(message)
//
//        adapter.setData(createNewData())
//        scrollToBottom()
//    }

    private fun uploadAudio(file: Files) {
        val fileName = DetailsMediaManager.MESSAGES + File.separator + DetailsMediaManager.fileName

    }


    private fun uploadMedia(message: Message) {
        val fileName = DetailsMediaManager.MESSAGES + File.separator + DetailsMediaManager.fileName
//        val observer = transferUtility?.upload(
//            ConfigManager.getAWSBucketName(this@ChatDetails),     /* The bucket to upload to */
//            fileName,    /* The key for the uploaded object */
//            DetailsMediaManager.uploadFile        /* The file where the data to upload exists */
//        )

//        observerId = observer?.id
//        message.additionalData.observerId = observerId!!
//        observer?.setTransferListener(this@ChatDetails)
    }

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
                    setDateForMessage(it)
                    mutableListMessages.add(it)

                    adapter.setData(createNewData())
                    scrollToBottom()

                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                })
        )
    }


    private fun stopProgress(error: Throwable?) {
        DetailsMediaManager.editUploadsCounter(false, error)
        setupUploadFinished()
    }

    private fun setupUploadFinished() {
        imgSelectorImageView?.isEnabled = true
        sendMessageEditText?.isEnabled = true
        sendMessageImageView?.isEnabled = true
    }

    private fun pendingMessageUploaded() {
        setupUploadFinished()
    }

    private fun createNewData(): MutableList<Message> =
        mutableListMessages.asSequence().toMutableList()

//    private fun getSendingMessage(messageId: Int?): Message? =
//        mutableListMessages.asSequence().find { message -> message.additionalData.observerId == messageId }

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
//            setMedia(DetailsMediaManager.tmpUri)
            probaPdf(DetailsMediaManager.tmpUri)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ChatDetails, MainScreenActivity::class.java))
    }

    private fun setIsMediaPortrait(uri: Uri?) {
//        Glide.with(this).load(uri)
//            .into(object : SimpleTarget<Drawable>() {
//                override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
//                    val width = resource?.intrinsicWidth
//                    val height = resource?.intrinsicHeight
//                    if (width != null && height != null) {
//                        isMediaPortrait = height > width
//                    }
//                }
//            })
    }

    private fun probaPdf(uri: Uri?): File {
        val selectedImageUri = uri
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val document = Document()
        val f = File(Environment.getExternalStorageDirectory(), "PDF_Images.pdf")
        val directoryPath = android.os.Environment.getExternalStorageDirectory().toString()
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

        when (orientation.toInt()) {
            6 -> {
                isMediaPortrait = true
            }
            8 -> {
                isMediaPortrait = true
            }
            0 -> setIsMediaPortrait(uri)
            else -> {
                isMediaPortrait = false
            }
        }

        Glide.with(this).load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(imgSelectorImageView!!)

        sendMessageImageView?.setImageResource(R.drawable.arrow_send_blue)
        isMessageSendable = true
        hasMedia = true
    }

    private fun removeMedia() {
        isMessageSendable = false
        //  Glide.with(this).load(R.drawable.add_media).apply(RequestOptions.circleCropTransform()).into(imgSelectorImageView!!)
    }


    private var firebaseMessageBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val firebaseMessage = intent.getSerializableExtra(FIREBASE_MESSAGE_KEY) as FirebaseMessage?
            if (firebaseMessage?.contactID == contact?.id) {
                getMessages()
            }

            FirebaseUtils.putPushNotificationId(this@ChatDetails, firebaseMessage?.pushNotificationID)
        }
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
//            // try again clicked
            val message = intent.getSerializableExtra(CONTENT_KEY) as Message
//            message.additionalData.errorSending = false
//            message.additionalData.isSending = true
//            DetailsMediaManager.tmpId = message.additionalData.id
//            if (DetailsMediaManager.hasMedia(message)) {
//                hasMedia = true
//                DetailsMediaManager.tmpUri = Uri.parse(message.additionalData.uri)
//                DetailsMediaManager.uploadFile = message.additionalData.uploadFile
//                DetailsMediaManager.fileName = message.additionalData.imageName
//            }
            sendMessage(message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }


    fun getPathFromURI(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().path + "/" + split[1]
                } else {
                    val splitIndex = docId.indexOf(':', 1)
                    val tag = docId.substring(0, splitIndex)
                    val path = docId.substring(splitIndex + 1)
                    val nonPrimaryVolume = getPathToNonPrimaryVolume(context, tag)
                    if (nonPrimaryVolume != null) {
                        val result = nonPrimaryVolume + "/" + path
                        val file = File(result)
                        if (file.exists() && file.canRead()) {
                            return result
                        }
                    }
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null!!, null!!)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf<String>(split[1])
                return getDataColumn(context, contentUri!!, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            return getDataColumn(context, uri, null!!, null!!)
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }// File
        // MediaStore (and general)
        return null
    }

    fun getPathToNonPrimaryVolume(context: Context, tag: String): String? {
        val volumes = context.getExternalCacheDirs()
        if (volumes != null) {
            for (volume in volumes) {
                if (volume != null) {
                    val path = volume.getAbsolutePath()
                    if (path != null) {
                        val index = path.indexOf(tag)
                        if (index != -1) {
                            return path.substring(0, index) + tag
                        }
                    }
                }
            }
        }
        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri, selection: String?,
        selectionArgs: Array<String>
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf<String>(column)
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

}
