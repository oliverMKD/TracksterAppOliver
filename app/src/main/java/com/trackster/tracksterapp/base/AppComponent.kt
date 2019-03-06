package com.trackster.tracksterapp.base

import com.trackster.tracksterapp.injection.module.NetworkModule
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.connectivity.NetworkModuleNew
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModuleNew::class])
interface AppComponent {

    fun postApi(): PostApi
}