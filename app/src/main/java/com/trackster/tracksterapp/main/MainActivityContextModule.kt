package com.trackster.tracksterapp.main

import android.content.Context
import com.trackster.tracksterapp.base.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class MainActivityContextModule(private val context: Context) {

    @Provides
    @FragmentScope
    fun context(): Context {
        return context
    }
}