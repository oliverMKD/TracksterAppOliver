package com.trackster.tracksterapp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
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
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils
import java.io.File
import android.util.SparseIntArray

class MessageRecyclerAdapter(private val context: Activity, private var list: MutableList<Message>) :
    RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerViewHolder>() {

    var mediaPlayer: MediaPlayer? = null


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
        return if (list[position].senderId == PreferenceUtils.getUserId(context)) {
            1
        } else {
            0
        }

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
      
       if (TextUtils.isEmpty(message.createTime)) {
            holder.dateTextView.visibility = View.GONE
        } else {
            holder.dateTextView.visibility = View.VISIBLE

        }
        if (message.senderId == PreferenceUtils.getUserId(context))
            holder.messageRelativeLayout.setBackgroundResource(com.trackster.tracksterapp.R.drawable.rounded_rectangle_green)
        else
            holder.messageRelativeLayout.setBackgroundResource(com.trackster.tracksterapp.R.drawable.rounded_rectangle_orange)

        if (message.senderId == PreferenceUtils.getBrokerId(context)) {
            holder.received_name!!.text =
                PreferenceUtils.getBrokerName(context)
        }
        else if (message.senderId == PreferenceUtils.getCarrierId(context)){
            holder.received_name!!.text =
                PreferenceUtils.getCarrierName(context)
        }
        else{
            holder.sent_name!!.text =
                PreferenceUtils.getDriverName(context)
        }

        if (!TextUtils.isEmpty(message.file?.filename)) {
            holder.messageTextView.visibility = View.GONE
            if (message.file?.filename!!.contains(".pdf")) {
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
                        val fileIn = File(item)
                        val u = Uri.fromFile(fileIn)
                        holder.messageImageView.visibility = View.VISIBLE
                        if (message.senderId == PreferenceUtils.getUserId(context)) {
                            Glide.with(context)
                                .load(u)
                                .into(holder.messageImageView)
                        } else {
                            Glide.with(context)
                                .load(u)
                                .into(holder.messageImageView)
                        }
                    }
                }
            }
            else if (message.file?.filename!!.contains(".png")) {
                holder.messageImageView.visibility = View.VISIBLE
                val sharedPref = getApplicationContext().getSharedPreferences("preff", Context.MODE_PRIVATE)
                var modelString: MutableList<String?> = mutableListOf()
                val serializedObject = sharedPref.getString("png", null)
                if (serializedObject != null) {
                    val gson = Gson()
                    val type = object : TypeToken<List<String>>() {
                    }.type
                    modelString = gson.fromJson(serializedObject, type)
                }
                val iterator = modelString.listIterator()
                for (item in iterator) {
                    if (item!!.contains(message.file?.filename!!)) {
                        val fileIn = File(item)
                        val u = Uri.fromFile(fileIn)
                        if (message.senderId == PreferenceUtils.getUserId(context)) {
                            Glide.with(context)
                                .load(u)
                                .into(holder.messageImageView)
                        } else {
                            Glide.with(context)
                                .load(u)
                                .into(holder.messageImageView)
                        }
                      
       

                
            }
            else  {
                holder.messagePlayImageView.visibility = View.VISIBLE
                holder.messageSeekBar.visibility = View.VISIBLE
                val sharedPref = getApplicationContext().getSharedPreferences("preff", Context.MODE_PRIVATE)
                var modelString: MutableList<String?> = mutableListOf()
                val serializedObject = sharedPref.getString("aac", null)
                if (serializedObject != null) {
                    val gson = Gson()
                    val type = object : TypeToken<List<String>>() {
                    }.type
                    modelString = gson.fromJson(serializedObject, type)
                }
                val iterator = modelString.listIterator()
                for (item in iterator) {
                    if (item!!.contains(message.file?.filename!!)) {
                        val fileIn = File(item)
                        val u = Uri.fromFile(fileIn)
                        if (message.senderId == PreferenceUtils.getUserId(context)) {
                            holder.messagePlayImageView.setOnClickListener {
                                setButtonPlayRecordingListener(fileIn.toString(), holder)
                            }
                        } else {
                            holder.messagePlayImageView.setOnClickListener {
                                setButtonPlayRecordingListener(fileIn.toString(), holder)
                            }
                        }

                    }
                }
            }

        } else {
            holder.messageTextView.visibility = View.VISIBLE

        }

        if (!TextUtils.isEmpty(message.content)) {
            holder.messageTextView.visibility = View.VISIBLE
            holder.messageTextView.text = message.content
} 
              else {
            holder.messageTextView.visibility = View.GONE
        }

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
        holder.messagePlayImageView.setOnClickListener() {
            if (holder.messagePlayImageView.text.toString().equals(context.getString(R.string.playRecord), true)) {
                holder.messagePlayImageView.text = context.getString(R.string.stopPlayingRecord)
                playRecording(source, holder)
            } else {
                holder.messagePlayImageView.text = context.getString(R.string.playRecord)
                stopPlayingRecording()
            }
        }
    }

    private fun playRecording(source: String, holder: MessageRecyclerViewHolder) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(source)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener {
            holder.messagePlayImageView.text = context.getString(R.string.playRecord)
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