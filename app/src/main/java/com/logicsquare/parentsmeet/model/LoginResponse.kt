package com.logicsquare.parentsmeet.model

data class LoginResponse (
    var error: Boolean = false,
    var reason: String="",
    val token : String,
    val user : User
)