package com.logicsquare.parentsmeet.model

data class User (

    val _id : String,
    val name : Name,
    val email : String,
    val phoneCountryCode : String,
    val phone : String,
    val userType : String,
    val relation : String
)