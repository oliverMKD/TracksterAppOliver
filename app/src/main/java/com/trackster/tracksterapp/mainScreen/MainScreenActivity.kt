package com.trackster.tracksterapp.mainScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.priyankvasa.android.cameraviewex.ErrorLevel
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.chat.ChatDetails
import com.trackster.tracksterapp.mainScreen.fragments.Current_Load
import com.trackster.tracksterapp.mainScreen.fragments.DetailsLoad
import com.trackster.tracksterapp.mainScreen.fragments.HistoryList
import com.trackster.tracksterapp.mainScreen.fragments.ProfileSettings
import com.trackster.tracksterapp.network.BaseResponse
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.connectivity.NoConnectivityException
import com.trackster.tracksterapp.selectTrailer.fragments.SelectColorFragment
import com.trackster.tracksterapp.selectTrailer.fragments.SelectTruckFragment
import com.trackster.tracksterapp.utils.DialogUtils
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_pane.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.app_bar_main_screen.*
import kotlinx.android.synthetic.main.nav_header_main_screen.*

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MainScreenActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
    View.OnClickListener {


    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var latLngOrigin: LatLng
    private lateinit var latLngDestination: LatLng
    private lateinit var mapsId: String
    private var mDelayHandler: Handler? = null
    var compositeDisposableContainer = CompositeDisposable()


    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    lateinit var apiService: PostApi

    private val compositeDisposable = CompositeDisposable()

    internal val mRunnable: Runnable = Runnable {


        setUpMap()


        val url = getUrl(LatLng(-122.5, 37.7), LatLng(-122.5, 37.7))
        getRoute(url)

    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        // 3
        private const val REQUEST_CHECK_SETTINGS = 2
    }


    private val currentLoad: Current_Load = Current_Load.newInstance()
    private val profileSettings: ProfileSettings = ProfileSettings.newInstance()
    private val historyList: HistoryList = HistoryList.newInstance()
    private val detailsList: DetailsLoad = DetailsLoad.newInstance()
    private val selectTracksFragment: SelectTruckFragment = SelectTruckFragment.newInstance()
    private val selectColorFragment: SelectColorFragment = SelectColorFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setSupportActionBar(toolbar)
        getChats()
        hamburger.visibility = View.VISIBLE
        hamburger.setOnClickListener(this)
        chat.setOnClickListener(this)

        floatBtn.setOnClickListener { view ->

            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.popup_window, null)
            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )
            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
                popupWindow.setOutsideTouchable(true);
            }
            // If API level 23 or higher then execute the code
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.BOTTOM
                popupWindow.enterTransition = slideIn
                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.BOTTOM
                popupWindow.exitTransition = slideOut
            }
            // Get the widgets reference from custom view
            //  val tv = view.findViewById<TextView>(R.id.text_view)
            val buttonPopup = view.findViewById<Button>(R.id.button_popup)
            // Set click listener for popup window's text view


            // Set a click listener for popup's button widget
            buttonPopup.setOnClickListener {
                // Dismiss the popup window
                popupWindow.dismiss()
            }
            // Set a dismiss listener for popup window
            popupWindow.setOnDismissListener {
                Toast.makeText(applicationContext, "Popup closed", Toast.LENGTH_SHORT).show()
            }
            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(root_layout)
            popupWindow.showAtLocation(
                root_layout, // Location to display popup window
                Gravity.BOTTOM, // Exact position of layout to display popup
                -120,// X offset
                20 // Y offset
            )
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
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


    public fun show() {
        attach.visibility = View.VISIBLE
        hamburger.visibility = View.VISIBLE
        floatBtn.show()
        chat.visibility = View.VISIBLE
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.hamburger -> {
                drawer_layout.openDrawer(Gravity.START)
                getUserInfo()
            }
            R.id.chat -> {
                startActivity(Intent(this@MainScreenActivity, ChatDetails::class.java))
            }
            R.id.attach -> {

            }
        }
    }



    private fun getUserInfo() {
        apiService = PostApi.create(this!!)
        compositeDisposableContainer.add(
            apiService.getInfoUser(
                PreferenceUtils.getAuthorizationToken(this!!)
            ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                {
                    name_user.setText(it.body()!!.firstName+" "+it.body()!!.lastName)

                    Glide.with(this!!)
                        .load(it.body()!!.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView_Drawer)
                }, {
                    Log.d("test", "error" + it.localizedMessage)
                }
            )
        )
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap!!.uiSettings.isZoomControlsEnabled = false
        googleMap!!.uiSettings.isMyLocationButtonEnabled = false
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, 5000)
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


    private fun getWeightStations() {
        apiService = PostApi.create(this)
        val coordinates = "42.0151079,21.4526962"
        val newCoordinates = coordinates.replace("\\,", "%2C")

        compositeDisposable.add(
            apiService.getWeighStations(
                PreferenceUtils.getAuthorizationToken(this), newCoordinates, 1
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //                Log.d("station", " "+ it[0].location)
                },
                    {
                        //                showProgress(false)
//                    handleApiError(it)
                    })
        )
    }

    private fun getChats() {
        apiService = PostApi.create(this@MainScreenActivity)
        compositeDisposable.add(
            apiService.getChats(
                PreferenceUtils.getAuthorizationToken(this)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mapsId = it[0].id
                    PreferenceUtils.saveChatId(this, mapsId)
//                getChatById(mapsId)
                    getWeightStations()

                    //                Log.d("station", " "+ it[0].location)
                }, {
                    //                showProgress(false)
                    handleApiError(it)
                })

        )
    }

    private fun getChatById(id: String) {
        apiService = PostApi.create(this@MainScreenActivity)
        compositeDisposable.add(
            apiService.getChatById(PreferenceUtils.getAuthorizationToken(this), id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val a = it.pickupAddress.location.lat
                    val b = it.pickupAddress.location.long
                    val c = it.destinationAddress.location.lat
                    val d = it.destinationAddress.location.long
                    latLngOrigin = LatLng(a, b)
                    latLngDestination = LatLng(c, d)
                    this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin))
                    this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination))
                    this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))

                    //                Log.d("station", " "+ it[0].location)
                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                    //                showProgress(false)
//                    Utils.handleApiError(it)
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
                        this@MainScreenActivity,
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

    private fun getUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude


        // Api Key
        val key = R.string.google_maps_key
        val sensor = "key=" + "AIzaSyD2kBqzaanSMkp9iX9J2JFtm1c7LjPFNW4"

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

    fun hide() {
        attach?.visibility = View.INVISIBLE
        hamburger?.visibility = View.INVISIBLE
        chat?.visibility = View.INVISIBLE
        floatBtn.hide()
    }


    private fun openCurrentLoadFragment() {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (currentLoad.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, currentLoad)

        } else {
            fragmentTransaction.add(R.id.fragment_container, currentLoad)
            fragmentTransaction.addToBackStack("currentLoadFragment")

        }
        fragmentTransaction.commit()
    }

    private fun openProfileSettingsFragment() {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()


        if (profileSettings.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, profileSettings)

        } else {
            fragmentTransaction.add(R.id.fragment_container, profileSettings)
            fragmentTransaction.addToBackStack("profileSettingsFragment")

        }
        fragmentTransaction.commit()
    }

    private fun openHistoryList() {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (historyList.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, historyList)

        } else {
            fragmentTransaction.add(R.id.fragment_container, historyList)
            fragmentTransaction.addToBackStack("historyListFragment")
        }
        fragmentTransaction.commit()
    }


    private fun opendetailsList() {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()


        if (detailsList.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, detailsList)

        } else {
            fragmentTransaction.add(R.id.fragment_container, detailsList)
            fragmentTransaction.addToBackStack("detailsListFragment")

        }
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.map -> {

            }
            R.id.routes -> {
                opendetailsList()
                hide()

            }
            R.id.loads -> {
                openCurrentLoadFragment()
                hide()
            }
            R.id.history -> {
                openHistoryList()
                hide()
            }
            R.id.settings -> {
                openProfileSettingsFragment()
                hide()

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    public fun openSelectTruckFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (selectTracksFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, selectTracksFragment)
        } else {
            fragmentTransaction.add(R.id.fragment_container, selectTracksFragment)
            fragmentTransaction.addToBackStack("selectTrucksFragment")
        }

        fragmentTransaction.commit()
    }

    public fun openSelectColorFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (selectColorFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, selectColorFragment)
        } else {
            fragmentTransaction.add(R.id.fragment_container, selectColorFragment)
            fragmentTransaction.addToBackStack("selectColorFragment")
        }

        fragmentTransaction.commit()
    }

    fun getSelectedTruck(id: String) {
        Toast.makeText(this@MainScreenActivity, "You selected : " + " truck", Toast.LENGTH_LONG).show()
    }
}
