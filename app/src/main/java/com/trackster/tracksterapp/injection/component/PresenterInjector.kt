package com.trackster.tracksterapp.injection.component

import com.trackster.tracksterapp.base.BaseView
import com.trackster.tracksterapp.injection.module.ContextModule
import com.trackster.tracksterapp.injection.module.NetworkModule
import com.trackster.tracksterapp.ui.login.LoginPresenter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(ContextModule::class), (NetworkModule::class)])
interface PresenterInjector {
    /**
     * Injects required dependencies into the specified PostPresenter.
     * @param postPresenter PostPresenter in which to inject the dependencies
     */
    fun inject(loginPresenter: LoginPresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}