package com.trackster.tracksterapp

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import com.bumptech.glide.Glide
import com.trackster.tracksterapp.Pane.LoginPane
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
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
//        Glide.with(this)
//            .load(R.drawable.trucklogo)
//            .centerInside()
//            .into(splash_image)



        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        checkIfUserIsLogged()
        //Navigate with delay

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
    private fun checkIfUserIsLogged(){
        val userId = PreferenceUtils.getAuthorizationToken(this)
        if(userId!=null && userId.isNotEmpty()){
            startActivity(Intent(this@SplashScreenActivity, MainScreenActivity::class.java))
            finish()

        } else{
            startActivity(Intent(this@SplashScreenActivity, LoginPane::class.java))
            finish()
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
