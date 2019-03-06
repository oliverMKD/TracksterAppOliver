package com.trackster.tracksterapp.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

abstract class BaseMainActivity : AppCompatActivity() {
    object PERMISSION {
        const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
        const val MY_PERMISSION_CAMERA = 200
    }

    private val GALLERY = 1
    private val CAMERA = 2

    abstract fun getLayoutId(): Int

    abstract fun isUpNavigationEnabled(): Boolean

    abstract fun getToolbar(): Toolbar?

    abstract fun hasToolbar(): Boolean

    abstract fun hasTitle(): Boolean

    private fun setupToolbar() {
        setSupportActionBar(getToolbar())

        supportActionBar?.setDisplayShowTitleEnabled(hasTitle())
        supportActionBar?.setDisplayHomeAsUpEnabled(isUpNavigationEnabled())
        supportActionBar?.setHomeButtonEnabled(isUpNavigationEnabled())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        if (hasToolbar()) {
            setupToolbar()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isUpNavigationEnabled()) {
            onBackPressed()
            return true
        }

        return super.onSupportNavigateUp()
    }

    fun hasReadContactsPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return true
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION.MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            // Permission has already been granted
            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION.MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    RxBus.publish(ReadContactsPermissionEvent(true))
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    RxBus.publish(ReadContactsPermissionEvent(false))
                }
                return
            }

            PERMISSION.MY_PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        (supportFragmentManager.fragments.last() as BaseMainViewFragment)?.onActivityResult(requestCode, resultCode, data)
    }

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION.MY_PERMISSION_CAMERA)
    }
}