package com.trackster.tracksterapp.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
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
import com.bumptech.glide.request.RequestOptions
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.utils.CONTENT_KEY
import com.trackster.tracksterapp.utils.TRY_AGAIN_BROADCAST
import com.trackster.tracksterapp.utils.Utils

class MessageRecyclerAdapter(private val context: Activity, private var list: MutableList<Message>,
                             private var avatar: String?)
    : RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerViewHolder>() {

    private var messageParamsLandscape = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)
    private var messageParamsPortrait = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

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

        val avatarImageView = view.findViewById(R.id.avatar_image_view) as ImageView

        val messageRelativeLayout = view.findViewById(R.id.message_relative_layout) as RelativeLayout
        val messageTextView = view.findViewById(R.id.message_text_view) as TextView
        val messageImageView = view.findViewById(R.id.message_image_view) as ImageView
        val messageVideoImageView = view.findViewById(R.id.message_video_image_view) as ImageView

        val timeTextView = view.findViewById(R.id.time_text_view) as TextView
        val timeImageView = view.findViewById(R.id.time_image_view) as ImageView
        val timeTryAgainTextView = view.findViewById(R.id.time_try_again_text_view) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerViewHolder {
        if (viewType == 1) {
            return MessageRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_message_item, parent, false))
        }
        return MessageRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.friend_message_item, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].isMine) {
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
            if (message.isPortrait) holder.messageImageView.layoutParams = messageParamsPortrait
            else holder.messageImageView.layoutParams = messageParamsLandscape

    private fun setContent(message: Message, holder: MessageRecyclerViewHolder) {
        holder.messageVideoImageView.visibility = View.GONE
        holder.timeTryAgainTextView.visibility = View.GONE
        holder.messageTextView.visibility = View.GONE
        holder.messageImageView.visibility = View.GONE

        if (TextUtils.isEmpty(message.sendTime)) {
            holder.dateTextView.visibility = View.GONE
        } else {
            holder.dateTextView.visibility = View.VISIBLE
            holder.dateTextView.text = message.sendTime
        }

        when (true) {
            message.additionalData.isSending -> setSendingUI(holder)
            message.additionalData.errorSending -> setErrorMessageUI(holder)
            else -> {
                holder.timeImageView.setImageResource(R.drawable.ic_sent)

                if (message.isMine) holder.messageRelativeLayout.setBackgroundResource(R.drawable.rounded_message_background)
                else holder.messageRelativeLayout.setBackgroundResource(R.drawable.rounded_friend_message_background)

                if (message.read && message.isMine) {
                    holder.timeTextView.text = context.getString(R.string.seen)
                    holder.timeTextView.setTextColor(Utils.getColor(context, R.color.colorSeenMessage))
                } else {
                    holder.timeTextView.text = message.additionalData.time
                    holder.timeTextView.setTextColor(Utils.getColor(context, R.color.colorAvatarBackground))
                }

                if (Utils.hasMessageMedia(message)) {
                    holder.messageImageView.visibility = View.VISIBLE
                    if (!TextUtils.isEmpty(message.imageUrl)) {
                        Glide.with(context)
                                .load(message.imageUrl)
                                .into(holder.messageImageView)
                    }

                    if (!TextUtils.isEmpty(message.videoUrl)) {
                        holder.messageVideoImageView.visibility = View.VISIBLE
                        Glide.with(context)
                                .load(message.videoUrl)
                                .into(holder.messageImageView)
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(message.content)) {
            holder.messageTextView.visibility = View.VISIBLE
            holder.messageTextView.text = message.content
        }
        Glide.with(context).load(avatar)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder))
                .apply(RequestOptions.errorOf(R.drawable.ic_placeholder))
                .apply(RequestOptions.circleCropTransform()).into(holder.avatarImageView)

        if (!TextUtils.isEmpty(message.additionalData.uri)) {
            holder.messageImageView.visibility = View.VISIBLE
            Glide.with(context).load(Uri.parse(message.additionalData.uri)).into(holder.messageImageView)
        }
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

    private fun setSendingUI(holder: MessageRecyclerViewHolder) {
        holder.timeTextView.text = context.getString(R.string.sending)
        holder.timeImageView.setImageResource(R.drawable.sending_animation)
        val rocketAnimation = holder.timeImageView.drawable as AnimationDrawable
        rocketAnimation.start()
        holder.messageRelativeLayout.setBackgroundResource(R.drawable.rounded_sending_message_background)
    }

    private fun setErrorMessageUI(holder: MessageRecyclerViewHolder) {
        holder.timeTextView.text = context.getString(R.string.message_not_sent)
        holder.timeTextView.setTextColor(Utils.getColor(context, R.color.colorAvatarBackground))
        holder.timeImageView.setImageResource(R.drawable.ic_not_sent)
        holder.timeTryAgainTextView.visibility = View.VISIBLE

        holder.messageRelativeLayout.setBackgroundResource(R.drawable.rounded_error_message_background)
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

    fun removeMessage(key: Int?) {
        val message = list.find { message -> message.additionalData.id == key }
        if (message != null) {
            list.remove(message)
        }
    }

//    private fun openMediaDetails(message: Message) {
//        val intent = Intent(context, MediaDetailsActivity::class.java)
//        intent.putExtra(Constants.MEDIA_DETAILS_IMAGE_URL_KEY, message.imageUrl)
//        intent.putExtra(Constants.MEDIA_DETAILS_VIDEO_URL_KEY, message.videoUrl)
//        (context as ChatDetailsActivity).openMediaDetails(intent)
//    }
}