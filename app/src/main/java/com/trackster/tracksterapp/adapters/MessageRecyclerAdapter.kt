package com.trackster.tracksterapp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getApplicationContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.utils.DateFormat
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils
import java.io.File


class MessageRecyclerAdapter(private val context: Activity, private var list: MutableList<Message>) :
    RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerViewHolder>() {

//    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//        if (fromUser)
//        {
//            mediaPlayer?.seekTo(progress)
//        }
//    }
//
//    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    var mediaPlayer: MediaPlayer? = null

    private val MSG_UPDATE_SEEK_BAR = 1845
    private val uiUpdateHandler : Handler? = null

    private var messageParamsLandscape = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    )
    private var messageParamsPortrait = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    )

    init {
        createLayouts(Utils.getScreenWidth(context))
    }

    private fun createLayouts(screenWidth: Int?) {
        val imageWidthHeight = screenWidth!! * 0.66

        val imageWidthPortrait = imageWidthHeight * 0.75
        messageParamsPortrait.height = imageWidthHeight.toInt()
        messageParamsPortrait.width = imageWidthPortrait.toInt()

        val imageHeightLandscape = imageWidthHeight * 0.75
        messageParamsLandscape.height = imageHeightLandscape.toInt()
        messageParamsLandscape.width = imageWidthHeight.toInt()
    }



    inner class MessageRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView = view.findViewById(R.id.date_text_view) as TextView
        val messageRelativeLayout = view.findViewById(R.id.message_relative_layout) as RelativeLayout
        val messageTextView = view.findViewById(R.id.message_text_view) as TextView
        val messageImageView = view.findViewById(R.id.message_image_view) as ImageView
        val messagePlayImageView = view.findViewById(R.id.ivPlayPause) as Button
        val messageSeekBar = view.findViewById(R.id.sbProgress) as SeekBar
        val received_name = view.findViewById(R.id.received_name) as TextView?
        val sent_name = view.findViewById(R.id.sent_name) as TextView?
        val timeTextView = view.findViewById(R.id.time_text_view) as TextView
        val timeTryAgainTextView = view.findViewById(R.id.time_try_again_text_view) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerViewHolder {
        if (viewType == 1) {
            return MessageRecyclerViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.my_message_item,
                    parent,
                    false
                )
            )
        }
        return MessageRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.friend_message_item,
                parent,
                false
            )
        )


    }

    override fun getItemViewType(position: Int): Int {

        if (list[position].senderId == PreferenceUtils.getUserId(context)) {
            return 1
        }
        return 0
    }

    override fun onBindViewHolder(@NonNull holder: MessageRecyclerViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val message = list[position]

        setViewsLayoutParams(message, holder)
        setContent(message, holder)

//        setClickListener(message, holder)
    }

    private fun setViewsLayoutParams(message: Message, holder: MessageRecyclerViewHolder) =
        if (message.senderId == PreferenceUtils.getUserId(context)) holder.messageImageView.layoutParams =
            messageParamsPortrait
        else holder.messageImageView.layoutParams = messageParamsLandscape

    private fun setContent(message: Message, holder: MessageRecyclerViewHolder) {
//        holder.messageVideoImageView.visibility = View.GONE
        holder.timeTryAgainTextView.visibility = View.GONE
//        holder.messageTextView.visibility = View.GONE
        holder.messageImageView.visibility = View.GONE
        holder.dateTextView.visibility = View.VISIBLE
        holder.dateTextView.text = message.createTime
        holder.messagePlayImageView.setBackgroundResource(R.drawable.play_audio)

      //  holder.messageSeekBar.setMax(mediaPlayer!!.getDuration())



        if (TextUtils.isEmpty(message.createTime)) {
            holder.dateTextView.visibility = View.GONE
        } else {
            holder.dateTextView.visibility = View.VISIBLE

        }
        if (message.senderId == PreferenceUtils.getUserId(context))
            holder.messageRelativeLayout.setBackgroundResource(com.trackster.tracksterapp.R.drawable.rounded_rectangle_green)
        else
            holder.messageRelativeLayout.setBackgroundResource(com.trackster.tracksterapp.R.drawable.rounded_rectangle_orange)

        when {
            message.senderId == PreferenceUtils.getBrokerId(context) -> holder.received_name!!.text = PreferenceUtils.getBrokerName(context)
            message.senderId == PreferenceUtils.getCarrierId(context) -> holder.received_name!!.text = PreferenceUtils.getCarrierName(context)
            else -> holder.sent_name!!.text = PreferenceUtils.getDriverName(context)
        }

        if (message.file != null  && message.content.isEmpty()) {
            if (!message.file!!.filename!!.isEmpty() && message.content.isEmpty()) {
                holder.messageTextView.visibility = View.GONE
                if (message.file?.filename!!.contains(".pdf") || message.file?.filename!!.contains(".png")) {
                    val sharedPref = getApplicationContext().getSharedPreferences("preff", Context.MODE_PRIVATE)
                    var modelString: MutableList<String?> = mutableListOf()
                    val serializedObject = sharedPref.getString("sliki", null)
                    if (serializedObject != null) {
                        val gson = Gson()
                        val type = object : TypeToken<List<String>>() {
                        }.type
                        modelString = gson.fromJson(serializedObject, type)
                    }
                    val iterator = modelString.listIterator()
                    for (item in iterator) {
                        if (item!!.contains(message.file?.filename!!)) {
                            val sharedPreferences = getApplicationContext().getSharedPreferences("preff", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            val gson = Gson()
                            val json = gson.toJson(modelString)
                            editor.putString("sliki", json)
                            editor.apply()
                            val fileIn = File(item)
                            val u = Uri.fromFile(fileIn)
                            holder.messageImageView.visibility = View.VISIBLE
                            if (message.senderId == PreferenceUtils.getUserId(context)) {
                                Glide.with(context)
                                    .asBitmap()
                                    .load(u)
                                    .into(holder.messageImageView)
                            } else {
                                Glide.with(context)
                                    .asBitmap()
                                    .load(u)
                                    .into(holder.messageImageView)
                            }
                        }
                    }
                } else {
                    holder.messageImageView.visibility = View.GONE
                }
                if (message.file?.filename!!.contains(".aac") || message.file?.filename!!.contains(".mp3") ) {
                    holder.messagePlayImageView.visibility = View.VISIBLE
                    holder.messageSeekBar.visibility = View.VISIBLE
                    val sharedPref = getApplicationContext().getSharedPreferences("preff", Context.MODE_PRIVATE)
                    var modelAudio: MutableList<String?> = mutableListOf()
                    val serializedObject = sharedPref.getString("aac", null)
                    if (serializedObject != null) {
                        val gson = Gson()
                        val type = object : TypeToken<List<String>>() {
                        }.type
                        modelAudio = gson.fromJson(serializedObject, type)
                    }
                    val iterator = modelAudio.listIterator()
                    for (item in iterator) {
                        if (item!!.contains(message.file?.filename!!)) {
                            val sharedPreferences = getApplicationContext().getSharedPreferences("preff", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            val gson = Gson()
                            val json = gson.toJson(modelAudio)
                            editor.putString("aac", json)
                            editor.apply()
                            val fileIn = File(item)
                            val u = Uri.fromFile(fileIn)
                            if (message.senderId == PreferenceUtils.getUserId(context)) {
                                holder.messagePlayImageView.setOnClickListener {
                                    setButtonPlayRecordingListener(fileIn.toString(), holder)
                                }
                            } else holder.messagePlayImageView.setOnClickListener {
                                setButtonPlayRecordingListener(fileIn.toString(), holder)
                            }

                        }
                    }
                } else {
                    holder.messagePlayImageView.visibility = View.GONE
                    holder.messageSeekBar.visibility = View.GONE
                }

            } else {
                holder.messageTextView.visibility = View.VISIBLE
                holder.messageTextView.text = ""
            }
        } else {
            if (!TextUtils.isEmpty(message.content)) {
                holder.messageTextView.visibility = View.VISIBLE
                holder.messageTextView.text = message.content
            } else {
                holder.messageTextView.visibility = View.VISIBLE
                holder.messageTextView.text = ""
            }
        }

//        if (!TextUtils.isEmpty(message.content)) {
//            holder.messageTextView.visibility = View.VISIBLE
//            holder.messageTextView.text = message.content
//        } else {
//            holder.messageTextView.visibility = View.GONE
//        }

        var messageDate = DateFormat.formatDate(message.createTime, DateFormat.DATE_FORMAT_MESSAGE_DETAILS)
        messageDate = DateFormat.formatDateDetailsMessage(message.createTime, messageDate)
        var previousDate2: String = ""
        if (previousDate2 == messageDate) {
            message.createTime = ""
            holder.dateTextView.text = ""
        } else {
            previousDate2 = messageDate
        }
        holder.dateTextView.text = messageDate
        var messageTime = DateFormat.formatTime(message.createTime, DateFormat.TIME_FORMAT_MESSAGE_DETAILS)
        messageTime = DateFormat.formatTimeDetailsMessage(message.createTime, messageTime)
        holder.timeTextView.text = messageTime
    }





    private fun setButtonPlayRecordingListener(source: String, holder: MessageRecyclerViewHolder) {


        holder.messagePlayImageView.setOnClickListener {


            if (holder.messagePlayImageView.text.toString().equals(context.getString(R.string.playRecord), true)) {
                holder.messagePlayImageView.text = context.getString(R.string.stopPlayingRecord)
                holder.messagePlayImageView.setBackgroundResource(R.drawable.stop)
           //     mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
                playRecording(source, holder)
            } else {
             // holder. messageSeekBar.setOnSeekBarChangeListener(this)
//                holder.messageSeekBar.setEnabled(false)
//                holder.messageSeekBar.setProgress(0)
                holder.messagePlayImageView.text = context.getString(R.string.playRecord)
                holder.messagePlayImageView.setBackgroundResource(R.drawable.play_audio)
               // mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                stopPlayingRecording()
            }
        }
    }
//    private val mSeekbarUpdateHandler = Handler()
//    private val mUpdateSeekbar = object:Runnable {
//        public override fun run() {
//        //   messageSeekBar.setProgress(mediaPlayer!!.getCurrentPosition())
//            mSeekbarUpdateHandler.postDelayed(this, 50)
//        }
//    }

    private fun playRecording(source: String, holder: MessageRecyclerViewHolder) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(source)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener {
            holder.messagePlayImageView.text = context.getString(R.string.playRecord)
            holder.messagePlayImageView.setBackgroundResource(R.drawable.play_audio)
        }
    }

    private fun stopPlayingRecording() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

    }

    override fun getItemCount(): Int = list.size

    fun setData(contacts: MutableList<Message>) {
        list.clear()
        list.addAll(contacts)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        list.add(message)
        notifyDataSetChanged()
    }

    //    private fun openMediaDetails(message: Message) {
//        val intent = Intent(context, MediaDetailsActivity::class.java)
//        intent.putExtra(MEDIA_DETAILS_IMAGE_URL_KEY, message.imageUrl)
//        (context as ChatDetails).openMediaDetails(intent)
//    }
}