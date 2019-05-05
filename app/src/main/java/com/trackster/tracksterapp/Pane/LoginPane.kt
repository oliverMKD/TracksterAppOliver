package com.trackster.tracksterapp.Pane

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.requests.DeviceRegistrationRequest
import com.trackster.tracksterapp.network.requests.FbLoginRequest
import com.trackster.tracksterapp.network.requests.LoginRequestWithPhone
import com.trackster.tracksterapp.network.requests.ValidatePhoneRequest
import com.trackster.tracksterapp.selectTrailer.SelectTrailerActivity
import com.trackster.tracksterapp.ui.login.trailer.TrailerActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_pane.*
import java.util.*

class LoginPane : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private var logintext1: TextView? = null

    private var smstext: TextView? = null
    private var or: TextView? = null
    private var view1: View? = null
    private var view2: View? = null
    private var fcb: ImageView? = null
    private var gogbtn: ImageView? = null

    private var resendcode: TextView? = null
    private var validatetext1: TextView? = null
    private var loginbtn: Button? = null
    private var validatebtn: Button? = null
    lateinit var apiService: PostApi
    private var phone1: EditText? = null
    private var code1: EditText? = null
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var callbackManager: CallbackManager? = null


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pane)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        logintext1 = findViewById<TextView>(R.id.logintext)
        validatetext1 = findViewById<TextView>(R.id.validate_phone)
        resendcode = findViewById<TextView>(R.id.resendCode)


        smstext = findViewById<TextView>(R.id.smstext)
        fcb = findViewById<ImageView>(R.id.fbtn)
        gogbtn = findViewById<ImageView>(R.id.gogbtn)
        or = findViewById<TextView>(R.id.or)

        view1 = findViewById<View>(R.id.view2)
        view2 = findViewById<View>(R.id.view)

        phone1 = findViewById<EditText>(R.id.phonenumber)
        code1 = findViewById<EditText>(R.id.inputpassword)

        loginbtn = findViewById(R.id.loginBtn)
        loginbtn!!.setOnClickListener(this)

        validatebtn = findViewById(R.id.validatebtn)
        validatebtn!!.setOnClickListener(this)

        fcb!!.setOnClickListener(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }

        }
    }


    override fun onClick(p0: View?) {

        when (p0?.id) {
            R.id.loginBtn -> getPhoneNumber()
            R.id.fbtn -> loginFB()

            R.id.validatebtn -> validatePhone()

        }
    }


    private fun getPhoneNumber() {

        val number = phone1?.text.toString()
        authenticateUserWithPhone(number)
        logintext1!!.visibility = View.INVISIBLE
        validatetext1!!.visibility = View.VISIBLE
        validatebtn!!.visibility = View.VISIBLE
        loginbtn!!.visibility = View.INVISIBLE

        smstext!!.visibility = View.VISIBLE
        or!!.visibility = View.INVISIBLE
        view2!!.visibility = View.INVISIBLE
        view1!!.visibility = View.INVISIBLE
        fcb!!.visibility = View.INVISIBLE
        gogbtn!!.visibility = View.INVISIBLE
        resendcode!!.visibility = View.VISIBLE
    }

    private fun validatePhone() {

        val number = phone1?.text.toString()
        val code = code1?.text.toString()
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjN2FjOTBiOWNlNGJhMDhjMjlhNzJiNiIsImNvbXBhbnlJZCI6IjVjNmMwMWI3ZjRlNWYzMWMzYzkxYzc4MCIsImZpcnN0TmFtZSI6Ik9saXZlciIsImxhc3ROYW1lIjoiQm96aW5vdnNraSIsInVzZXJUeXBlIjo2LCJpYXQiOjE1NTY5NjkzNjIsImV4cCI6MTU1NzU3NDE2Mn0.OYtvY3k8BXENNpDj8GYL6LkaG3GWoavkMEtu8PLGscg"
        val id = "5c7ac90b9ce4ba08c29a72b6"
        PreferenceUtils.saveUserId(this@LoginPane, id)

        PreferenceUtils.saveAuthorizationToken(this@LoginPane, token)
        startActivity(Intent(this@LoginPane, SelectTrailerActivity::class.java))

//        validateWithPhone(number, code) // ova ke go aktivirame koga ke vrakja token po SMS
    }

    private fun registerFCM(token: String) {
        apiService = PostApi.create(this)
        CompositeDisposable().add(
            apiService.registerDevice(
                PreferenceUtils.getAuthorizationToken(this@LoginPane),
                DeviceRegistrationRequest(token)
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                    startActivity(Intent(this@LoginPane, TrailerActivity::class.java))

                },
                    {


                        Log.d("pane", "error")
                    })
        )
    }

    private fun authenticateUserWithPhone(phone: String) {
        apiService = PostApi.create(this)
        CompositeDisposable().add(

            apiService.loginWithPhone(LoginRequestWithPhone(phone))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                    startActivity(Intent(this@LoginPane, TrailerActivity::class.java))

                }, {


                    Log.d("pane", "error")
                })
        )
    }


    private fun validateWithPhone(phone: String, code: String) {
        apiService = PostApi.create(this)
        CompositeDisposable().add(
            apiService.validatePhone(ValidatePhoneRequest(phone, code))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                    startActivity(Intent(this@LoginPane, TrailerActivity::class.java))

                }, {


                    Log.d("pane", "error")
                })
        )
    }


    fun loginFB() {

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
                    authenticateWithFB(loginResult.accessToken.token)
                    logintext1!!.visibility = View.INVISIBLE
                    validatetext1!!.visibility = View.VISIBLE
                    validatebtn!!.visibility = View.VISIBLE
                    loginbtn!!.visibility = View.INVISIBLE

                    smstext!!.visibility = View.VISIBLE
                    or!!.visibility = View.INVISIBLE
                    view2!!.visibility = View.INVISIBLE
                    view1!!.visibility = View.INVISIBLE
                    fcb!!.visibility = View.INVISIBLE
                    gogbtn!!.visibility = View.INVISIBLE
                    resendcode!!.visibility = View.VISIBLE
                    startActivity(Intent(this@LoginPane, SelectTrailerActivity::class.java))
                }

                override fun onCancel() {
                    Log.d("MainActivity", "Facebook onCancel.")
                }

                override fun onError(error: FacebookException) {
                    Log.d("MainActivity", "Facebook onError.")
                }
            })
    }


    private fun authenticateWithFB(access_token: String) {
        apiService = PostApi.create(this)
        CompositeDisposable().add(
            apiService.loginFB(FbLoginRequest(access_token))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    PreferenceUtils.saveAuthorizationToken(this, it.body()!!.token)
                    PreferenceUtils.saveUserId(this, it.body()!!.id)
                    getFCMToken()
                    startActivity(Intent(this@LoginPane, MainScreenActivity::class.java))

                }, {
                    Log.d("pane", "error")
                })
        )
    }

    fun getFCMToken() {
        var refreshedToken = FirebaseInstanceId.getInstance().getToken()
        Log.d("tokenFCM", "FCM token: " + refreshedToken)
        registerFCM(refreshedToken!!)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}
