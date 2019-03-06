package com.trackster.tracksterapp.base

import android.support.multidex.MultiDexApplication
import com.trackster.tracksterapp.network.OkHttpClientModule
import com.trackster.tracksterapp.network.connectivity.NetworkModuleNew
import io.reactivex.disposables.CompositeDisposable

class App : MultiDexApplication() {

    private var appComponent: AppComponent? = null
    val compositeDisposable = CompositeDisposable()



    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModuleNew(NetworkModuleNew())
            .okHttpClientModule(OkHttpClientModule())
            .build()
    }


    fun getAppComponent(): AppComponent {
        return appComponent!!
    }
}