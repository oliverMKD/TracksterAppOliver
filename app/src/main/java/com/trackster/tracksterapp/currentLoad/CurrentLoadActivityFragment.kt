package com.trackster.tracksterapp.currentLoad

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trackster.tracksterapp.R

/**
 * A placeholder fragment containing a simple view.
 */
class CurrentLoadActivityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_load, container, false)
    }
}