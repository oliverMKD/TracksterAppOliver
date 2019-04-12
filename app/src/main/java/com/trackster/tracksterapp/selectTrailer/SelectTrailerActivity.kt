package com.trackster.tracksterapp.selectTrailer

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.adapters.OtherTrailersRecyclerAdapter
import com.trackster.tracksterapp.adapters.TrailersRecyclerAdapter
import com.trackster.tracksterapp.model.Trailers
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SelectTrailerActivity : AppCompatActivity(), OnMapReadyCallback,View.OnClickListener {



    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var listView: RecyclerView
    lateinit var recyclerViewOthers : RecyclerView
    private lateinit var adapter: TrailersRecyclerAdapter
    private lateinit var adapterOthers : OtherTrailersRecyclerAdapter
    lateinit var apiService: PostApi
    private lateinit var mOthers : TextView

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_trailer)
        listView = findViewById(R.id.recyclerViewTrailer)
        recyclerViewOthers = findViewById(R.id.recyclerViewTrailerOthers)
        adapter = TrailersRecyclerAdapter(this@SelectTrailerActivity)
        adapterOthers = OtherTrailersRecyclerAdapter(this@SelectTrailerActivity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mOthers = findViewById(R.id.text_other)
        mOthers.setOnClickListener(this)
        getTrailers()
        getOtherTrailers()
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.text_other-> recyclerViewOthers.visibility = View.VISIBLE
        }
    }
    private fun initRecyclerView(list : ArrayList<Trailers>) {
        listView.setHasFixedSize(true)
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter
        adapter.setData(list)
    }
    private fun initRecyclerViewOthers(list : ArrayList<Trailers>) {
        recyclerViewOthers.setHasFixedSize(true)
        recyclerViewOthers.layoutManager = LinearLayoutManager(this)
        recyclerViewOthers.adapter = adapterOthers
        adapterOthers.setData(list)
    }

    private fun getTrailers(){
        apiService = PostApi.create(this@SelectTrailerActivity)
        CompositeDisposable().add(apiService.getDefaultTrailers(
            PreferenceUtils.getAuthorizationToken(this))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                initRecyclerView(it)
// Log.d("station", " "+ it[0].location)
            }, {
                // showProgress(false)
//                Utils.handleApiError(it)
            })
        )
    }
    private fun getOtherTrailers(){
        apiService = PostApi.create(this@SelectTrailerActivity)
        CompositeDisposable().add(apiService.getOtherTrailers(
            PreferenceUtils.getAuthorizationToken(this))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                recyclerViewOthers.visibility = View.VISIBLE
                initRecyclerViewOthers(it)
// Log.d("station", " "+ it[0].location)
            }, {
                // showProgress(false)
//                Utils.handleApiError(it)
            })
        )
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
    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
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
    fun getToastMessage(id : String) {
        Toast.makeText(this@SelectTrailerActivity,"You selected : "+id + " trailer",Toast.LENGTH_LONG).show()
    }
}
