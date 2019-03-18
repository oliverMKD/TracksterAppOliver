package com.trackster.tracksterapp.model

import com.google.gson.annotations.SerializedName

    data class User(var email : String, var isActive : Boolean, var firstName : String,
                    var lastName : String,var image : String, var id : String, var token : String, var password : String,
                    var userType : Int, var company: Company)

