package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getApplicationContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.chat.ChatDetails
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.utils.CONTENT_KEY
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.TRY_AGAIN_BROADCAST
import com.trackster.tracksterapp.utils.Utils
import java.io.File


class MessageRecyclerAdapter(
    private val context: Activity,
    private var list: MutableList<Message>
) : RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerViewHolder>() {

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

//        val avatarImageView = view.findViewById(R.id.avatar_image_view) as ImageView

        val messageRelativeLayout = view.findViewById(R.id.message_relative_layout) as RelativeLayout
        val messageTextView = view.findViewById(R.id.message_text_view) as TextView
        val messageImageView = view.findViewById(R.id.message_image_view) as ImageView
        //        val messageVideoImageView = view.findViewById(R.id.message_video_image_view) as ImageView
        val received_name = view.findViewById(R.id.received_name) as TextView?
        val sent_name = view.findViewById(R.id.sent_name) as TextView?
        val timeTextView = view.findViewById(R.id.time_text_view) as TextView
        // val timeImageView = view.findViewById(R.id.time_image_view) as ImageView
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

    override fun onBindViewHolder(holder: MessageRecyclerViewHolder, position: Int) {
        val message = list[position]

        setViewsLayoutParams(message, holder)
        setContent(message, holder)
        setClickListener(message, holder)
    }

    private fun setViewsLayoutParams(message: Message, holder: MessageRecyclerViewHolder) =
        if (message.senderId == PreferenceUtils.getUserId(context)) holder.messageImageView.layoutParams =
            messageParamsPortrait
        else holder.messageImageView.layoutParams = messageParamsLandscape

    private fun setContent(message: Message, holder: MessageRecyclerViewHolder) {
//        holder.messageVideoImageView.visibility = View.GONE
        holder.timeTryAgainTextView.visibility = View.GONE
        holder.messageTextView.visibility = View.GONE
        holder.messageImageView.visibility = View.GONE

//        if (TextUtils.isEmpty(message.createTime)) {
//            holder.dateTextView.visibility = View.GONE
//        } else {
        holder.dateTextView.visibility = View.VISIBLE
        holder.dateTextView.text = message.createTime
//        }

        //  holder.timeImageView.setImageResource(R.drawable.ic_sent)

        if (message.senderId == PreferenceUtils.getUserId(context))
            holder.messageRelativeLayout.setBackgroundResource(com.trackster.tracksterapp.R.drawable.rounded_rectangle_green)
        else
            holder.messageRelativeLayout.setBackgroundResource(com.trackster.tracksterapp.R.drawable.rounded_rectangle_orange)

        if (message.senderId == PreferenceUtils.getBrokerId(context)) {
            holder.received_name!!.text = PreferenceUtils.getBrokerName(context)
        } else if (message.senderId == PreferenceUtils.getCarrierId(context)) {
            holder.received_name!!.text = PreferenceUtils.getCarrierName(context)
        } else if (message.senderId == PreferenceUtils.getUserId(context)) {
            holder.sent_name!!.text = PreferenceUtils.getDriverName(context)
        }

//                if (message.read && message.isMine) {
//                    holder.timeTextView.text = context.getString(R.string.seen)
//                    holder.timeTextView.setTextColor(Utils.getColor(context, R.color.colorSeenMessage))
//                } else {
////                    holder.timeTextView.text = message.additionalData.time
////                    holder.timeTextView.setTextColor(Utils.getColor(context, R.color.colorAvatarBackground))
//                }

//                if (Utils.hasMessageMedia(message)) {
//                    holder.messageImageView.visibility = View.VISIBLE
//                    if (!TextUtils.isEmpty(message.imageUrl)) {
//                        Glide.with(context)
//                                .load(message.imageUrl)
//                                .into(holder.messageImageView)
//                    }
        if (!TextUtils.isEmpty(message.file?.filename)) {
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
             if (message.file?.filename!!.contains(".png")){
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

        }
//
//                    if (!TextUtils.isEmpty(message.videoUrl)) {
//                        holder.messageVideoImageView.visibility = View.VISIBLE
//                        Glide.with(context)
//                                .load(message.videoUrl)
//                                .into(holder.messageImageView)
//                    }
//                }

        if (!TextUtils.isEmpty(message.content)) {
            holder.messageTextView.visibility = View.VISIBLE
            holder.messageTextView.text = message.content
        }
//        Glide.with(context).load(avatar)
//                .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder))
//                .apply(RequestOptions.errorOf(R.drawable.ic_placeholder))
//                .apply(RequestOptions.circleCropTransform()).into(holder.avatarImageView)

//        if (!TextUtils.isEmpty(message.additionalData.uri)) {
//            holder.messageImageView.visibility = View.VISIBLE
//            Glide.with(context).load(Uri.parse(message.additionalData.uri)).into(holder.messageImageView)
//        }
    }

    private fun setClickListener(message: Message, holder: MessageRecyclerViewHolder) {
        holder.timeTryAgainTextView.setOnClickListener {
            list.remove(message)
            val intent = Intent(TRY_AGAIN_BROADCAST)
            intent.putExtra(CONTENT_KEY, message)
            context.sendBroadcast(intent)
        }
        holder.messageImageView.setOnClickListener {
            //            openMediaDetails(message)
        }
    }

    private fun paneChat() {

        (context as ChatDetails).getMessages()
    }

    override fun getItemCount(): Int = list.size

    fun setData(contacts: MutableList<Message>) {
        list.clear()
        list.addAll(contacts)
        notifyDataSetChanged()
    }

    //    fun setAudioData(audio_files: MutableList<Files>) {
//        listAudio.clear()
//        listAudio.addAll(audio_files)
//        notifyDataSetChanged()
//    }
//
    fun addMessage(message: Message) {
        list.add(message)
        notifyDataSetChanged()
    }
//    fun addAudio(files: Files) {
//        listAudio.add(files)
//        notifyDataSetChanged()
//    }

//    fun removeMessage(key: Int?) {
////        val message = list.find { message -> message.additionalData.id == key }
//        if (message != null) {
//            list.remove(message)
//        }
//    }

//    private fun openMediaDetails(message: Message) {
//        val intent = Intent(context, MediaDetailsActivity::class.java)
//        intent.putExtra(MEDIA_DETAILS_IMAGE_URL_KEY, message.imageUrl)
//        (context as ChatDetails).openMediaDetails(intent)
//    }
}