package com.trackster.tracksterapp.model

data class Address(
    var city: String,
    var state: String,
    var area: String,
    var street: String,
    var zipCode: String,
    var location: GeoPoint
)