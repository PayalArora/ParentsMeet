package com.logicsquare.parentsmeet.network


import com.logicsquare.parentsmeet.model.LoginRequest
import com.logicsquare.parentsmeet.model.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse?>
}

