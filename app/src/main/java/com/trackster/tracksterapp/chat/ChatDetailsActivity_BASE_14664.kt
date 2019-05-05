package com.trackster.tracksterapp.chat

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.MessageRecyclerAdapter
import com.trackster.tracksterapp.base.BaseChatActivity
import com.trackster.tracksterapp.base.TracksterApplication
import com.trackster.tracksterapp.firebase.FirebaseUtils
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.model.AdditionalData
import com.trackster.tracksterapp.model.Contact
import com.trackster.tracksterapp.model.FirebaseMessage
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File


class ChatDetailsActivity :BaseChatActivity(),View.OnClickListener {

    object Day {
        const val TODAY = "today"
        const val YESTERDAY = "yesterday"
    }

    // UI components
    private var sendMessageRelativeLayout: RelativeLayout? = null
    private var imgSelectorImageView: ImageView? = null
    private var sendMessageImageView: ImageView? = null
    private var sendMessageEditText: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private lateinit var adapter: MessageRecyclerAdapter
    private var mutableListMessages: MutableList<Message> = mutableListOf()

    private var contact: Contact? = null
    private var sender: Contact? = null
    private var recipient: Contact? = null
    private var previousDate: String = ""
    private var isMessageSendable = false
    private var hasMedia = false
    private var isMediaPortrait = false
    private var observerId: Int? = null

    private var isActivityVisible = false

    // aws
    private var s3Client: AmazonS3Client? = null
//    private var transferUtility: TransferUtility? = null

    private var isMotherphone = false

    private val compositeDisposable = CompositeDisposable()
//    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    override fun getLayoutId(): Int = R.layout.activity_chat_details

    override fun isUpNavigationEnabled(): Boolean = true

    override fun hasToolbar(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMutualViews()


//            initAWS()
            initViews()
//            handleIntent()
            initMessageList()
        getMessages()

//            compositeDisposable.add(RxBus.listen(ClearPendingMessageEvent::class.java).subscribe {
//                pendingMessageUploaded()
//            })
//
//
//            listenForUploadReady()
//            if (FeedMediaManager.uploadsCounter > 0 || DetailsMediaManager.uploadsCounter > 0) {
//                setPaddingSendMessageRelativeLayout(true)
//            }
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
            sendMessageRelativeLayout?.setPadding(sendMessageViewPadding,
                sendMessageViewPadding,
                sendMessageViewPadding,
                resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin))
        } else {
            sendMessageRelativeLayout?.setPadding(sendMessageViewPadding,
                sendMessageViewPadding,
                sendMessageViewPadding,
                sendMessageViewPadding)
        }
    }
    private fun initMutualViews() {
        sendMessageRelativeLayout = findViewById(R.id.send_message_relative_layout)
        recyclerView = findViewById(R.id.recycler_view_details)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView?.layoutManager = linearLayoutManager
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun initAWS() {
        val s3Client = AmazonS3Client(BasicAWSCredentials(ConfigManager.getAWSAccessKey(this), ConfigManager.getAWSSecretKey(this)), Utils.getAWSConfigurationClient())
        this.s3Client = s3Client
//        transferUtility = TransferUtility(s3Client, applicationContext)
    }

    private fun handleIntent() {
        contact = intent.getSerializableExtra(CONTACT_KEY) as Contact?
        setToolbarTitleAndLogo(contact?.nickname, contact?.avatar)
    }
    private fun initMessageList() {
        adapter = MessageRecyclerAdapter(this, mutableListMessages, contact?.avatar)
        recyclerView?.adapter = adapter

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

        sendMessageEditText = findViewById(R.id.send_message_edit_text)

        sendMessageEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val message = sendMessageEditText?.text
                if (!TextUtils.isEmpty(message)) {
                    sendMessageImageView?.setImageResource(R.drawable.arrow_send_blue)
                    isMessageSendable = true
                } else {
                    if (isMessageSendable) {
                        sendMessageImageView?.setImageResource(R.drawable.arrow_send)
                        isMessageSendable = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
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


    private fun getMessages() {
        apiService = PostApi.create(this@ChatDetailsActivity)
        compositeDisposable.add(
            apiService.getChatById(PreferenceUtils.getAuthorizationToken(this@ChatDetailsActivity),
                PreferenceUtils.getChatId(this@ChatDetailsActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
//                    mutableListMessages = it.message
                    setData(it.message)
                    //                Log.d("station", " "+ it[0].location)
                }, {
                    Log.d("destinacija",""+ it.localizedMessage)
                    //                showProgress(false)
                })
        )
    }

    private fun setData(result: MutableList<Message>) {
        result.reverse()
//        setDatesData(result)
//        val pendingMessages = DetailsMediaManager.getPendingMessages(contact?.id)
//        result.addAll(pendingMessages)
        adapter.setData(result)
        scrollToBottom()
    }

    private fun setDatesData(list: MutableList<Message>) {
        for (message: Message in list) {
            setDateForMessage(message)
        }
    }

    private fun setDateForMessage(message: Message) {
//        val additionalData = AdditionalData()
//        additionalData.time = getString(R.string.validate) + " " + DateFormat.formatDate(message.sendTime, DateFormat.TIME_FORMAT_MESSAGE_DETAILS)
//
//        var messageDate = DateFormat.formatDate(message.sendTime, DateFormat.DATE_FORMAT_MESSAGE_DETAILS)
//        messageDate = DateFormat.formatDateDetailsMessage(message.sendTime, messageDate)
//        if (messageDate == Day.TODAY) {
//            messageDate = getString(R.string.Continue)
//        } else if (messageDate == Day.YESTERDAY) {
//            messageDate = getString(R.string.validate)
//        }
//        if (previousDate == messageDate) {
//            message.sendTime = ""
//        } else {
//            message.sendTime = messageDate
//            previousDate = messageDate
//        }
//
//        message.additionalData = additionalData
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_send_msg -> sendMessage(DetailsMediaManager.createMessage(
                this,sendMessageEditText?.text.toString(),"1234")!!)
//            R.id.img_selector_image_view -> addMedia()
//            R.id.send_message_image_view -> {
//                if (contact != null) {
//                    AnswersAnalyticsHandler.logChatDetailsUserAction("Send_Message", contact!!.id, this)
//
//                    if (isMessageSendable) {
//                        sendMessage(DetailsMediaManager.createMessage(this, contact!!.id, hasMedia, isMediaPortrait,
//                            sendMessageEditText?.text.toString())!!)
//                        clearMessage()
//                    }
//                }
//            }
        }
    }

    private fun clearMessage() {
        sendMessageEditText?.setText("")
        hasMedia = false
        removeMedia()
        sendMessageImageView?.setImageResource(R.drawable.arrow_send)
    }

    private fun sendMessage(message: Message) {
        if (hasMedia) {
            hasMedia = false

            DetailsMediaManager.editUploadsCounter(true, null)
            uploadMedia(message)

        } else {
            postMessage(message)
        }

        DetailsMediaManager.mutableListSendingMessages.add(message)
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

    private fun uploadMedia(message: Message) {
        val fileName = DetailsMediaManager.MESSAGES + File.separator + DetailsMediaManager.fileName
//        val observer = transferUtility?.upload(
//            ConfigManager.getAWSBucketName(this@ChatDetailsActivity),     /* The bucket to upload to */
//            fileName,    /* The key for the uploaded object */
//            DetailsMediaManager.uploadFile        /* The file where the data to upload exists */
//        )

//        observerId = observer?.id
//        message.additionalData.observerId = observerId!!
//        observer?.setTransferListener(this@ChatDetailsActivity)
    }

    private fun postMessage(message: Message) {

        apiService = PostApi.create(this@ChatDetailsActivity)
        compositeDisposable.add(
            apiService.postMessage(PreferenceUtils.getAuthorizationToken(this@ChatDetailsActivity),
                PreferenceUtils.getChatId(this@ChatDetailsActivity),message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                    mutableListMessages = it.message
                    //                Log.d("station", " "+ it[0].location)
                }, {
                    Log.d("destinacija",""+ it.localizedMessage)
                    //                showProgress(false)
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
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        hasMedia = false
        if (requestCode == DetailsMediaManager.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            DetailsMediaManager.setMediaDataGallery(this, data?.data!!)
            setMedia(data.data)
        } else if ((requestCode == DetailsMediaManager.REQUEST_IMAGE_CAPTURE || requestCode == DetailsMediaManager.REQUEST_VIDEO_CAPTURE)
            && resultCode == Activity.RESULT_OK) {
            setMedia(DetailsMediaManager.tmpUri)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ChatDetailsActivity, MainScreenActivity::class.java))
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
        Glide.with(this).load(R.drawable.add_media).apply(RequestOptions.circleCropTransform()).into(imgSelectorImageView!!)
    }

//    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
//    }

//    override fun onStateChanged(id: Int, state: TransferState?) {
//        if (state == TransferState.WAITING_FOR_NETWORK) {
//            messageNotSent(getSendingMessage(id)!!)
//            transferUtility?.cancel(id)
//            AnswersAnalyticsHandler.logError("S3_Error", "Waiting for network", this)
//            stopProgress(NoConnectivityException(getString(R.string.general_error_no_internet)))
//        } else if (state == TransferState.COMPLETED) {
//            val fileName = DetailsMediaManager.MESSAGES + File.separator + DetailsMediaManager.fileName
//            Log.d("Image_link", ConfigManager.getAWSUrl(this) + fileName)
//            val message = getSendingMessage(id)
//            if (message != null) {
//                postMessage(message)
//            } else {
//                stopProgress(null)
//            }
//
//        }
//    }
//
//    override fun onError(id: Int, ex: Exception?) {
//        AnswersAnalyticsHandler.logError("S3_Error", ex?.message, this)
//        stopProgress(ex)
//        messageNotSent(getSendingMessage(observerId)!!)
//    }

    private var firebaseMessageBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val firebaseMessage = intent.getSerializableExtra(FIREBASE_MESSAGE_KEY) as FirebaseMessage?
            if (firebaseMessage?.contactID == contact?.id) {
                getMessages()
            }

            FirebaseUtils.putPushNotificationId(this@ChatDetailsActivity, firebaseMessage?.pushNotificationID)
        }
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
//            // try again clicked
//            val message = intent.getSerializableExtra(CONTENT_KEY) as Message
//            message.additionalData.errorSending = false
//            message.additionalData.isSending = true
//            DetailsMediaManager.tmpId = message.additionalData.id
//            if (DetailsMediaManager.hasMedia(message)) {
//                hasMedia = true
//                DetailsMediaManager.tmpUri = Uri.parse(message.additionalData.uri)
//                DetailsMediaManager.uploadFile = message.additionalData.uploadFile
//                DetailsMediaManager.fileName = message.additionalData.imageName
//            }
//            sendMessage(message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}
