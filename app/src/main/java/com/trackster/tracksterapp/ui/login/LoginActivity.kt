package com.trackster.tracksterapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.KeyListener
import android.transition.Visibility
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.BaseActivity
import com.trackster.tracksterapp.network.requests.FbLoginRequest
import com.trackster.tracksterapp.network.requests.LoginRequest
import com.trackster.tracksterapp.ui.login.trailer.TrailerActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class LoginActivity : BaseActivity<LoginPresenter>(), LoginView, TextView.OnEditorActionListener, View.OnClickListener {


    private var mIsUserSigningIn: Boolean = false

    // UI references.
    private var mUsername: EditText? = null
    private var mPasswordView: EditText? = null
    private var mFacebook: RelativeLayout? = null
    private var mGoogle: RelativeLayout? = null
    private var mProgressView: View? = null
    private var mButton: Button? = null
    private var callbackManager: CallbackManager? = null
    private var mValidate: Button? = null
    private var mPhoneNumber: EditText? = null
    private var mSMS: EditText? = null
    private var mUsernameLayout : RelativeLayout? = null
    private var mPasswordLayout : RelativeLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        mButton!!.setOnClickListener(this)
        mFacebook!!.setOnClickListener(this)
        mValidate!!.setOnClickListener(this)
//        mPasswordView!!.setOnEditorActionListener(this)

    }

    fun initViews() {
        mUsername = findViewById(R.id.login_username)
        mPasswordView = findViewById(R.id.login_password)

        mFacebook = findViewById(R.id.login_with_fb_button)
        mProgressView = findViewById(R.id.login_progress)
        mButton = findViewById(R.id.kopceSledno)
        mValidate = findViewById(R.id.kopceValidate)
        mPhoneNumber = findViewById(R.id.phone_number_after_FB)
        mSMS = findViewById(R.id.SMS_after_FB)
        mUsernameLayout = findViewById(R.id.userNameLayout)
        mPasswordLayout = findViewById(R.id.passwordLayout)
    }

    override fun instantiatePresenter(): LoginPresenter {
        return LoginPresenter(this)
    }

    override fun showError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.login_activity

    private fun loginWithPhone(){
        val email = mPhoneNumber?.text.toString()
        val password = mSMS?.text.toString()
        authenticateUser(email, password)
    }

    private fun attemptLogin() {
        // Reset errors.
        mUsername?.error = null
        mPasswordView?.error = null

        // Store values at the time of the login attempt.
        val email = mUsername?.text.toString()
        val password = mPasswordView?.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView?.error = getString(R.string.error_field_required)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mUsername?.error = getString(R.string.error_field_required)
            focusView = mUsername
            cancel = true
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and perform the user login attempt.

            Utils.hideSoftKeyboard(this)
//            showProgress(true)

            authenticateUser(email, password)
        }

    }

    private fun authenticateUser(email: String, password: String) {
        CompositeDisposable().add(apiService.login(LoginRequest(email, password))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("proba", "success" + it.headers().get("x-auth-token"))
                val token = it.headers().get("x-auth-token")
                PreferenceUtils.saveAuthorizationToken(this, token!!)
                startActivity(Intent(this@LoginActivity,TrailerActivity::class.java))
//                showProgress(false)
//                navigateFurther()
            }, {
                //                showProgress(false)
                handleApiError(it)
            })
        )
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            mUsername?.keyListener = null
            mPasswordView?.keyListener = null
        } else {
            mUsername?.keyListener = mUsername?.tag as KeyListener
            mPasswordView?.keyListener = mPasswordView?.tag as KeyListener
        }

        mIsUserSigningIn = show
        mProgressView?.visibility = if (show) View.VISIBLE else View.GONE
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.kopceSledno -> attemptLogin()
            R.id.login_with_fb_button -> loginFacebook()
            R.id.kopceValidate -> loginWithPhone()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (v?.id) {
            R.id.login_password -> {
                if (
                    actionId == EditorInfo.IME_ACTION_DONE
                ) {
                    attemptLogin()
                    return true
                }
                return false
            }
        }

        return true
    }

    private fun authenticateWithFB(access_token : String){
        CompositeDisposable().add(apiService.loginFB(FbLoginRequest(access_token))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("proba", "success" + it.body()!!.token)
                val token = it.body()!!.token
                PreferenceUtils.saveAuthorizationToken(this, token)
                PreferenceUtils.saveUserId(this,it.body()!!.id)
                mUsername!!.visibility = View.GONE
                mPasswordView!!.visibility = View.GONE
                mFacebook!!.visibility = View.GONE
                mButton!!.visibility = View.GONE
                mValidate!!.visibility = View.VISIBLE
                mPhoneNumber!!.visibility = View.VISIBLE
                mSMS!!.visibility = View.VISIBLE
                mUsernameLayout!!.visibility = View.GONE
                mPasswordLayout!!.visibility = View.GONE
//                showProgress(false)
//                navigateFurther()
            }, {
                //                showProgress(false)
                handleApiError(it)
            })
        )
    }

    private fun loginFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
                    authenticateWithFB(loginResult.accessToken.token)

                }

                override fun onCancel() {
                    Log.d("MainActivity", "Facebook onCancel.")

                }

                override fun onError(error: FacebookException) {
                    Log.d("MainActivity", "Facebook onError.")

                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}


