package com.trackster.tracksterapp.base

import android.content.Context

interface BaseView{
    /**
     * Returns the context in which the application is running.
     * @return the context in which the application is running
     */
    fun getContext(): Context
}