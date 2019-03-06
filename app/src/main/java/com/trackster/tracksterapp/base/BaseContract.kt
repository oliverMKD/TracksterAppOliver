package com.trackster.tracksterapp.base

interface BaseContract {
    interface BaseView {

        fun setLoadingIndicator(active: Boolean)

        fun showError()

        fun showError(message: String)

        fun showSuccessMessage(message: String)

        fun handleApiError(throwable: Throwable)

    }

    interface BasePresenter {
        interface DataCallback {
            fun onFailure(throwable: Throwable)
        }

        fun unsubscribe()
    }

    interface BaseInteractor {

        fun unsubscribe()
    }
}