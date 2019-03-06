package com.trackster.tracksterapp.base

import com.trackster.tracksterapp.injection.component.DaggerPresenterInjector
import com.trackster.tracksterapp.injection.component.PresenterInjector
import com.trackster.tracksterapp.injection.module.ContextModule
import com.trackster.tracksterapp.injection.module.NetworkModule
import com.trackster.tracksterapp.network.requests.LoginRequest
import com.trackster.tracksterapp.ui.login.LoginPresenter

abstract class BasePresenter<out V : BaseView>(protected val view: V) {

    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .baseView(view)
        .contextModule(ContextModule)
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * This method may be called when the presenter view is created
     */
    open fun onViewCreated(){}

    /**
     * This method may be called when the presenter view is destroyed
     */
    open fun onViewDestroyed(){}

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is LoginPresenter -> injector.inject(this)
        }
    }

    abstract fun loginWithFB()
    abstract fun login(loginRequest: LoginRequest)
}