package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Routes(@SerializedName("overview_polyline") val overview_polyline : OverviewPoly,
                  @SerializedName("legs") val legs : ArrayList<Legs> )
