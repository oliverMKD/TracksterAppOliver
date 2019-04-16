package com.trackster.tracksterapp.selectTrailer

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.mainScreen.MainScreenActivity
import com.trackster.tracksterapp.selectTrailer.fragments.SelectColorFragment
import com.trackster.tracksterapp.selectTrailer.fragments.SelectTrailerFragment
import com.trackster.tracksterapp.selectTrailer.fragments.SelectTruckFragment

class SelectTrailerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val SPLASH_DELAY: Long = 1500 //1.5 seconds
    private var mDelayHandler: Handler? = null
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            goToSelectTruckFragment()
        }
    }

    private val selectTrailerFragment : SelectTrailerFragment = SelectTrailerFragment.newInstance()
    private val selectTracksFragment : SelectTruckFragment = SelectTruckFragment.newInstance()
    private val selectColorFragment : SelectColorFragment = SelectColorFragment.newInstance()


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val FRAGMENT_CHOOSE_MORE_ITEMS = "fragment_choose_more_items"

    }
    private fun openSelectTrailerFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (selectTrailerFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container_select_trailer, selectTrailerFragment)
        } else {
            fragmentTransaction.add(R.id.fragment_container_select_trailer, selectTrailerFragment)
            fragmentTransaction.addToBackStack("selectTrailerFragment")
        }

        fragmentTransaction.commit()
    }
    private fun openSelectTruckFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (selectTracksFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container_select_trailer, selectTracksFragment)
            fragmentTransaction.remove(selectTrailerFragment)
        } else {
            fragmentTransaction.add(R.id.fragment_container_select_trailer, selectTracksFragment)
            fragmentTransaction.addToBackStack("selectTrucksFragment")
            fragmentTransaction.remove(selectTrailerFragment)
        }

        fragmentTransaction.commit()
    }
    private fun openSelectColorFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (selectColorFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container_select_trailer, selectColorFragment)
            fragmentTransaction.remove(selectTracksFragment)
        } else {
            fragmentTransaction.add(R.id.fragment_container_select_trailer, selectColorFragment)
            fragmentTransaction.addToBackStack("selectColorFragment")
            fragmentTransaction.remove(selectTracksFragment)
        }

        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_trailer)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        openSelectTrailerFragment()


// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

    fun getSelectedTrailer(id: String) {
        Toast.makeText(this@SelectTrailerActivity, "You selected : " + id + " trailer", Toast.LENGTH_LONG).show()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }
    fun getSelectedTruck(id: String) {
        Toast.makeText(this@SelectTrailerActivity, "You selected : " + id + " truck", Toast.LENGTH_LONG).show()
        openSelectColorFragment()
    }
    fun getSelectedColor() {
        Toast.makeText(this@SelectTrailerActivity, "You selected :  color", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@SelectTrailerActivity, MainScreenActivity::class.java))
    }

    private fun goToSelectTruckFragment() {
        openSelectTruckFragment()
    }
}
