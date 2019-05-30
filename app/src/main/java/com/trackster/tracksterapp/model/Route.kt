package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

data class Route
    (@SerializedName("routes") var routes :List<Routes> )