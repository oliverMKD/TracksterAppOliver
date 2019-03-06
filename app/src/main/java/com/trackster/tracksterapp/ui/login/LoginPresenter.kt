package com.trackster.tracksterapp.ui.login

import android.content.Context
import android.util.Log
import com.trackster.tracksterapp.base.BasePresenter
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.requests.LoginRequest
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter(loginView: LoginView): BasePresenter<LoginView>(loginView) {


    @Inject
    lateinit var postApi: PostApi

    private var subscription: Disposable? = null


    private val mCompositeDisposable = CompositeDisposable()

    override fun onViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewDestroyed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override  fun  loginWithFB(){

    }
    override fun login(loginRequest: LoginRequest){
//        mCompositeDisposable.add(postApi.login(loginRequest.email,loginRequest. password).observeOn(AndroidSchedulers.mainThread()).subscribeOn(
//            Schedulers.io())
//            .subscribe({
//                Log.d("proba","token is : "+it.accessToken)
////                PreferenceUtils.saveAuthorizationToken(Context, user)
////                PreferenceUtils.saveUserData(this@LoginActivity, user)
////                FirebaseUtils.sendFirebaseTokenToServer(this@LoginActivity, PreferenceUtils.getFirebaseToken(this@LoginActivity))
////                showProgress(false)
////                navigateFurther()
//            }, {
//                Log.d("proba","error")
//                //                showProgress(false)
////                handleApiError(error)
//            }))

    }
}