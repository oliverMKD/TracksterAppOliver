package com.trackster.tracksterapp.main

import android.content.Context
import com.trackster.tracksterapp.network.PostApi
import dagger.Module
import dagger.Provides

@Module(includes = [MainActivityContextModule::class])
class MainPresenterModule(private val view: MainContract.View) {

    @Provides
    fun presenter(context: Context, postApi: PostApi): MainContract.Presenter {
        return MainPresenterImpl(view, context, postApi)
    }
}