package com.trackster.tracksterapp

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import com.bumptech.glide.Glide
import com.trackster.tracksterapp.ui.login.LoginActivity
import com.trackster.tracksterapp.ui.login.trailer.TrailerActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashScreenActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            checkIfUserIsLogged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getHash()
        Glide.with(this)
            .load(R.drawable.icon)
            .centerCrop()
            .into(splash_image)



        mDelayHandler = Handler()

//        checkIfUserIsLogged()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
    private fun checkIfUserIsLogged(){
        val userId = PreferenceUtils.getUserId(this)
        if(userId!=null && userId.isNotEmpty()){
            startActivity(Intent(this@SplashScreenActivity, TrailerActivity::class.java))

        } else{
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        }
    }

   private fun getHash(){
        try {
            val info = packageManager.getPackageInfo(
                "com.trackster.tracksterapp",
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:",""+ Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }
}
