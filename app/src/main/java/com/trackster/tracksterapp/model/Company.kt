package com.trackster.tracksterapp.model

data class Company(
    var id: String,
    var name: String,
    var emailDomain: String,
    var phone: String,
    var physicalAddress: String,
    var mailingAddress: String,
    var companyType: Int,
    var usDotNumber: String,
    var dunsNumber: String,
    var stateCarrierId: String
)