package com.trackster.tracksterapp.Pane

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.EditText
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
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.requests.FbLoginRequest
import com.trackster.tracksterapp.network.requests.LoginRequestWithPhone
import com.trackster.tracksterapp.ui.login.trailer.TrailerActivity
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_pane.*
import java.util.*

class LoginPane : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {


    lateinit var apiService: PostApi
    var phone1 = findViewById<EditText>(R.id.phonenumber)

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

      when (p0?.id){
          R.id.loginBtn-> getPhoneNumber()
          R.id.fbtn->loginFB()

      }
    }


    private fun getPhoneNumber() {

        val number = phone1?.text.toString()
        authenticateUserWithPhone(number)
    }


    private fun authenticateUserWithPhone(phone: String) {
        CompositeDisposable().add(
            apiService.loginWithPhone(LoginRequestWithPhone(phone))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    startActivity(Intent(this@LoginPane, TrailerActivity::class.java))

                }, {


                    Log.d("pane", "error")
                })
        )
    }


    fun loginFB (){

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


    private fun authenticateWithFB(access_token : String){
        CompositeDisposable().add(apiService.loginFB(FbLoginRequest(access_token))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
            }, {
                Log.d("pane", "error")
            })
        )
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}
