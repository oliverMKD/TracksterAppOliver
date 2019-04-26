package com.trackster.tracksterapp.chat

import android.os.Bundle
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseChatActivity

class ChatDetailsActivity :BaseChatActivity() {
    override fun getLayoutId(): Int = R.layout.activity_chat_details

    override fun isUpNavigationEnabled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasToolbar(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
