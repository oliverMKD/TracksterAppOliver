package com.trackster.tracksterapp.ui.login.maps

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.network.BaseResponse
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.connectivity.NoConnectivityException
import com.trackster.tracksterapp.network.requests.FbLoginRequest
import com.trackster.tracksterapp.ui.login.LoginActivity
import com.trackster.tracksterapp.utils.DialogUtils
import com.trackster.tracksterapp.utils.PreferenceUtils
import com.trackster.tracksterapp.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var latLngOrigin: LatLng
    private lateinit var latLngDestination: LatLng
    private lateinit var mapsId: String
    private var mDelayHandler: Handler? = null


    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    lateinit var apiService: PostApi

    internal val mRunnable: Runnable = Runnable {


        this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin))
        this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination))
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))
        setUpMap()
        val url = getUrl(LatLng(-122.5, 37.7), LatLng(-122.5, 37.7))
        getRoute(url)
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        // 3
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.trackster.tracksterapp.R.layout.activity_maps)
        val intent = intent
        if (intent.hasExtra("idMaps")) {
            mapsId = intent.getStringExtra("idMaps")
            Log.d("mapaaa", intent.getStringExtra("idMaps"))
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }
//
        createLocationRequest()


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap!!.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener(this)
        getChatById(mapsId)

        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, 5000)

    }

    override fun onMarkerClick(p0: Marker?) = false

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
        googleMap!!.isMyLocationEnabled = true

        googleMap!!.mapType = GoogleMap.MAP_TYPE_TERRAIN

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
//                getWeightStations(currentLatLng)
                placeMarkerOnMap(currentLatLng)
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.5f))
            }
        }
    }

    private fun getWeightStations(latLng: LatLng) {

        apiService = PostApi.create(this)

        val token = PreferenceUtils.getAuthorizationToken(this)
        CompositeDisposable().add(
            apiService.getWeighStations(
                PreferenceUtils.getAuthorizationToken(this), "application/json", latLng, 1
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                Log.d("station", " "+ it[0].location)
                }, {
                    //                showProgress(false)
                    handleApiError(it)
                })
        )
    }

    private fun getChatById(id: String) {
        apiService = PostApi.create(this@MapsActivity)
        CompositeDisposable().add(
            apiService.getChatById(
                PreferenceUtils.getAuthorizationToken(this), id
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("details", "" + it)
                    val a = it.pickupAddress.location.lat
                    val b = it.pickupAddress.location.long
                    val c = it.destinationAddress.location.lat
                    val d = it.destinationAddress.location.long
                    latLngOrigin = LatLng(a, b)
                    latLngDestination = LatLng(c, d)

                    //                Log.d("station", " "+ it[0].location)
                }, {
                    //                showProgress(false)
                    Utils.handleApiError(it)
                })
        )
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)

        val titleStr = getAddress(location)  // add these two lines
        markerOptions.title(titleStr)
        // 2
        googleMap!!.addMarker(markerOptions)
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
//            BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))

    }

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this@MapsActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }


    private fun getUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude


        // Api Key
        val key = R.string.google_maps_key
        val sensor = "key=" + "AIzaSyAAiXz26AXC1vFu6e2dl1TIi9hZEEiI22Y"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"


        return url
    }

    private fun getRoute(url: String) {


        val path: MutableList<List<LatLng>> = ArrayList()
        val directionsRequest = object : StringRequest(
            Request.Method.GET, url, Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                // Get routes
                val routes = jsonResponse.getJSONArray("routes")
                if (routes.length() > 0) {
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val steps = legs.getJSONObject(0).getJSONArray("steps")
                    for (i in 0 until steps.length()) {
                        val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                        path.add(com.google.maps.android.PolyUtil.decode(points))
                    }
                    for (i in 0 until path.size) {
                        this.googleMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                    }
                } else {
                    Log.d("no_path_found", "No path found")
                }


            }, Response.ErrorListener { _ ->
            }) {}
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
    }

    fun handleApiError(error: Throwable?) {
        when (error) {
            is HttpException -> {
                try {
                    val response = Gson().fromJson(error.response().errorBody()?.string(), BaseResponse::class.java)
                    val errorMessage = response?.errorMessages?.get(0)
                    if (TextUtils.isEmpty(errorMessage)) {
                        DialogUtils.showGeneralErrorMessage(this)
                    } else {
                        DialogUtils.showErrorMessage(this, errorMessage)
                    }
                } catch (exception: JsonSyntaxException) {
                    DialogUtils.showGeneralErrorMessage(this)
                }
            }
            is NoConnectivityException -> {
                DialogUtils.showErrorMessage(this, getString(R.string.general_error_no_internet))
            }
            is SocketTimeoutException -> {
                DialogUtils.showGeneralErrorMessage(this)
            }
            else -> {
                DialogUtils.showGeneralErrorMessage(this)
            }
        }
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


}
