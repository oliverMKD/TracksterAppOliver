package com.trackster.tracksterapp.mainScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.*
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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.google.maps.android.PolyUtil
import com.shockwave.pdfium.PdfiumCore
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.cameraToPdf.CameraActivity
import com.trackster.tracksterapp.chat.ChatDetails
import com.trackster.tracksterapp.mainScreen.fragments.*
import com.trackster.tracksterapp.model.Message
import com.trackster.tracksterapp.network.BaseResponse
import com.trackster.tracksterapp.network.PostApi
import com.trackster.tracksterapp.network.connectivity.NoConnectivityException
import com.trackster.tracksterapp.utils.DialogUtils
import com.trackster.tracksterapp.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.app_bar_main_screen.*
import kotlinx.android.synthetic.main.nav_header_main_screen.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.*
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
    var listMessagesCheckSize: MutableList<Message> = mutableListOf()
    var model: ArrayList<String?> = arrayListOf()

    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    lateinit var apiService: PostApi

    private val compositeDisposable = CompositeDisposable()

    private val mRunnable: Runnable = Runnable {
        setUpMap()
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
    private val selectTracksFragment: SelectTruckFragmentDouble = SelectTruckFragmentDouble.newInstance()
    private val selectColorFragment: SelectColorFragmentDouble = SelectColorFragmentDouble.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setSupportActionBar(toolbar)
        hamburger.visibility = View.VISIBLE
        hamburger.setOnClickListener(this)
        chat.setOnClickListener(this)
        attach.setOnClickListener(this)

        floatBtn.setOnClickListener {

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
                popupWindow.isOutsideTouchable = true;
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

    fun show() {
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
                val intent = Intent(this@MainScreenActivity, ChatDetails::class.java)
                intent.putStringArrayListExtra("testPreLoad", model)
                startActivity(intent)
            }
            R.id.attach -> {
                startActivity(Intent(this@MainScreenActivity, CameraActivity::class.java))
            }
        }
    }

    private fun getUserInfo() {
        apiService = PostApi.create(this@MainScreenActivity)
        compositeDisposableContainer.add(
            apiService.getInfoUser(
                PreferenceUtils.getAuthorizationToken(this@MainScreenActivity)
            ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                {
                    name_user.text = "${it.body()!!.firstName} ${it.body()!!.lastName}"

                    Glide.with(this@MainScreenActivity)
                        .load(it.body()!!.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView_Drawer)
                }, {
                    handleApiError(it.cause)
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
        mDelayHandler!!.postDelayed(mRunnable, 1500)
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

                getWeightStations(currentLatLng)
                getChats()
            }
        }
    }

    private fun getWeightStations(location: LatLng) {
        apiService = PostApi.create(this)
        val coordinates = location.latitude.toString() + "," + location.longitude.toString()
        val newCoordinates = coordinates.replace("\\,", "%2C")
        compositeDisposable.add(
            apiService.getWeighStations(
                PreferenceUtils.getAuthorizationToken(this), newCoordinates, 100
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val iterator = it.listIterator()
                    for (item in iterator) {
                        val lat = item.location.lat
                        val lng = item.location.long
                        val markerOptions = MarkerOptions().position(LatLng(lat, lng))
                        googleMap!!.addMarker(markerOptions)
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.weighstations))
                    }
                },
                    {
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
                    getChatById(mapsId)
                }, {
                    handleApiError(it.cause)
                })
        )
    }

    @SuppressLint("LogNotTimber")
    private fun getChatById(id: String) {
        apiService = PostApi.create(this@MainScreenActivity)
        compositeDisposable.add(
            apiService.getChatById(PreferenceUtils.getAuthorizationToken(this), id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    val iterator = it.pickupAddress.listIterator()
                    iterator.forEach { item ->
                        val lat = item.location.lat
                        val lng = item.location.long
                        latLngOrigin = LatLng(lat, lng)
                    }
                    val iteratorDestination = it.destinationAddress.listIterator()
                    iteratorDestination.forEach { item ->
                        val lat = item.location.lat
                        val lng = item.location.long
                        latLngDestination = LatLng(lat, lng)
                    }
                    this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.warehouse))
                    this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.warehouse))
                    this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))
                    val iteratorPoly = it.routes.listIterator()
                    iteratorPoly.forEach { item ->
                        val route = item.routes
                        val iteratorRoute = route.listIterator()
                        iteratorRoute.forEach { i ->
                            val poly = i.overview_polyline
                            val polyString = poly.points
                            val aa = PolyUtil.decode(polyString)
                            val ruta = PolylineOptions()
                            (0 until aa.size)
                                .forEach { x ->
                                    ruta.add(LatLng(aa[x].latitude,aa[x].longitude))
                                }
                            ruta.color(Color.BLUE)
                            googleMap!!.addPolyline(ruta)
                        }
                    }
                    val brokerName = it.broker.firstName
                    val brokerLastName = it.broker.lastName
                    val brokerFullName = "$brokerName $brokerLastName"
                    val driverName = it.driver.firstName
                    val driverLastName = it.driver.lastName
                    val driverFullName = "$driverName $driverLastName"
                    val carrierName = it.carrier.firstName
                    val carrierLastName = it.carrier.lastName
                    val carrierFullName = "$carrierName $carrierLastName"
                    val brokerId = it.broker.id
                    val carrierId = it.carrier.id
                    PreferenceUtils.saveBrokerName(this@MainScreenActivity, brokerFullName)
                    PreferenceUtils.saveDriverName(this@MainScreenActivity, driverFullName)
                    PreferenceUtils.saveCarrierName(this@MainScreenActivity, carrierFullName)
                    PreferenceUtils.saveBrokerId(this@MainScreenActivity, brokerId)
                    PreferenceUtils.saveCarrierId(this@MainScreenActivity, carrierId)
                    if (it.message.size == PreferenceUtils.getSize(this@MainScreenActivity)) {
                    } else {
                        val aa = (it.message.size - PreferenceUtils.getSize(this@MainScreenActivity)!!)
                        val list: List<Message> = it.message.takeLast(aa)
                        val message = list.listIterator()
                        message.forEach { item ->
                            listMessagesCheckSize.add(item)
                            val preffList = PreferenceUtils.getSize(this@MainScreenActivity)
                            val sumList = (preffList!! + 1)
                            PreferenceUtils.saveMessSize(this@MainScreenActivity, sumList)
                            if (item.file != null) {
                                if (item.file!!.filename != null) {
                                    doAsync {
                                        getFileFromServer(item.file!!.filename!!)
                                    }.execute()
                                } else {
                                    Log.e("getFileFromServer", "1111")
                                }
                            } else {
                                Log.d("getFileFromServer", "22222")
                            }
                        }
                    }
                },
                    {
                        Log.d("getFileFromServer", "22222")
                    })
        )
    }


    @SuppressLint("LogNotTimber")
    private fun getFileFromServer(filename: String) {
        apiService = PostApi.create(this@MainScreenActivity)
        compositeDisposable.add(
            apiService.getFileById(
                PreferenceUtils.getAuthorizationToken(this@MainScreenActivity),
                PreferenceUtils.getChatId(this@MainScreenActivity), filename
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val writtenToDisk = writeResponseBodyToDisk(it.body(), filename)
                    Log.d("writtenToDisk", "" + writtenToDisk.toString())

                }, {
                    Log.d("destinacija", "" + it.localizedMessage)
                })
        )
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }

    @SuppressLint("LogNotTimber")
    private fun writeResponseBodyToDisk(body: ResponseBody?, fileName: String): Boolean {
        try {
            val retrofitBetaFile = File(getExternalFilesDir(null).toString() + File.separator + fileName)
            Timber.e(retrofitBetaFile.path)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body?.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body?.byteStream()
                outputStream = FileOutputStream(retrofitBetaFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("writeResponseBodyToDisk", "file download: $fileSizeDownloaded of $fileSize")
                }


                outputStream.flush()

                when {
                    fileName.contains(".pdf") -> {
                        val uri = Uri.fromFile(retrofitBetaFile)
                        generateImageFromPdf(uri)
                    }
                    fileName.contains(".png") -> {
                        val pngString = retrofitBetaFile.toString()
                        val sharedPref = applicationContext.getSharedPreferences(getString(R.string.preff), Context.MODE_PRIVATE)
                        var modelString: MutableList<String?> = mutableListOf()
                        val serializedObject = sharedPref.getString(getString(R.string.sliki), null)
                        if (serializedObject != null) {
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {
                            }.type
                            modelString = gson.fromJson(serializedObject, type)
                        }
                        modelString.add(pngString)
                        model.add(pngString)
                        val sharedPreferences = getSharedPreferences(getString(R.string.preff), MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(modelString)
                        editor.putString(getString(R.string.sliki), json)
                        editor.apply()
                    }
                    fileName.contains(".aac") || fileName.contains(".mp3") -> {
                        val audioString = retrofitBetaFile.toString()
                        val sharedPref = applicationContext.getSharedPreferences(getString(R.string.preff), Context.MODE_PRIVATE)
                        var modelString: MutableList<String?> = mutableListOf()
                        val serializedObject = sharedPref.getString(getString(R.string.aac), null)
                        if (serializedObject != null) {
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {
                            }.type
                            modelString = gson.fromJson(serializedObject, type)
                        }
                        modelString.add(audioString)
                        val sharedPreferences = getSharedPreferences(getString(R.string.preff), MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json = gson.toJson(modelString)
                        editor.putString(getString(R.string.aac), json)
                        editor.apply()
                    }
                }

                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }
    }

    private fun generateImageFromPdf(pdfUri: Uri) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this)
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            val fd = contentResolver.openFileDescriptor(pdfUri, "r")
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            saveImage(bmp, pdfUri.toString())
            pdfiumCore.closeDocument(pdfDocument) // important!
        } catch (e: Exception) {

        }
    }

    private fun saveImage(bmp: Bitmap, name: String) {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        val folder = Environment.getExternalStorageDirectory().toString() + "/PDF"
        var out: FileOutputStream? = null
        try {
            val newFolder = File(folder)
            if (!newFolder.exists())
                newFolder.mkdirs()
            val domain: String? = name.substringAfterLast("/")
            val file = File(newFolder, "$domain.png")
            val sharedPref = applicationContext.getSharedPreferences(getString(R.string.preff), Context.MODE_PRIVATE)
            var modelString: MutableList<String?> = mutableListOf()
            val serializedObject = sharedPref.getString(getString(R.string.sliki), null)
            if (serializedObject != null) {
                val gson = Gson()
                val type = object : TypeToken<List<String>>() {
                }.type
                modelString = gson.fromJson(serializedObject, type)
            }
            modelString.add(file.toString())
            model.add(file.toString())
            val sharedPreferences = getSharedPreferences(getString(R.string.preff), MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(modelString)
            editor.putString(getString(R.string.sliki), json)
            editor.apply()
            out = FileOutputStream(file)

            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale;
            val inputStream = FileInputStream(file)

            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)

            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // bmp is your Bitmap instance
        } catch (e: Exception) {
            //todo with exception
        } finally {
            try {
                out?.close()
            } catch (e: Exception) {
                //todo with exception
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)

        val titleStr = getAddress(location)  // add these two lines
        markerOptions.title(titleStr)
        // 2
        googleMap!!.addMarker(markerOptions)
    }

    @SuppressLint("LogNotTimber")
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

    public fun openProfileSettingsFragment() {
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


    private fun openDetailsList() {
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
                openDetailsList()
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

    public fun openSelectTruckFragmentDouble() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (selectTracksFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, selectTracksFragment)
            fragmentTransaction.remove(profileSettings)
        } else {
            fragmentTransaction.add(R.id.fragment_container, selectTracksFragment)
            fragmentTransaction.addToBackStack("selectTrucksFragment")
            fragmentTransaction.remove(profileSettings)
        }
        fragmentTransaction.commit()
    }

    public fun openSelectColorFragmentDouble() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(profileSettings)
        if (selectColorFragment.isAdded) {
            fragmentTransaction.replace(R.id.fragment_container, selectColorFragment)
        } else {
            fragmentTransaction.add(R.id.fragment_container, selectColorFragment)
            fragmentTransaction.addToBackStack("selectColorFragment")
            fragmentTransaction.remove(profileSettings)
        }
        fragmentTransaction.commit()
    }

    fun getSelectedTruck(id: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        Toast.makeText(this@MainScreenActivity, "You selected : $id truck", Toast.LENGTH_LONG).show()
        fragmentTransaction.remove(selectTracksFragment)
        openProfileSettingsFragment()
        fragmentTransaction.commit()
    }

    fun getSelectedColorDouble(name: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        Toast.makeText(this@MainScreenActivity, "You selected : $name color", Toast.LENGTH_LONG).show()
        fragmentTransaction.remove(selectColorFragment)
        openProfileSettingsFragment()
        fragmentTransaction.commit()
    }
}
