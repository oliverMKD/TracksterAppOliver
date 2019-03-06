package com.trackster.tracksterapp.ui.login.loadDetails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.trackster.tracksterapp.R

class LoadDetailsActivity : AppCompatActivity() {

    private lateinit var mText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_details)
        mText = findViewById(R.id.textDetali)
        mText.text =
            "ОВДЕ ДЕТАЛИ ЗА СЕКОЈ ТОВАР, АМА ЌЕ ЧЕКАМ ДА ВИДАМ ШТО ЌЕ ВРАЌА АПИ-то И КОИ ПОДАТОЦИ ДА СЕ ИСПИШУВААТ"
    }
}