package com.logicsquare.parentsmeet.network


import com.logicsquare.parentsmeet.model.*
import com.logicsquare.parentsmeet.ui.SignUpActivity
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse?>

    @POST("/api/v1/register")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<LoginResponse?>

    @POST("/api/v1/get/otp")
    fun getOtp(@Body otpRequest: OtpRequest): Call<LoginResponse?>

    @POST("/api/v1/verify/otp")
    fun submitOtp(@Body otpRequest: SubmitOtpRequest): Call<LoginResponse?>

    @GET("api/v1/user/details")
    fun getProfile(@Header("Authorization") Authorization:String): Call<ProfileResponse?>

    @POST("api/v1/kid/details")
    fun addKid(@Header("Authorization") Authorization:String,@Body addKidRequest: AddKidRequest): Call<AddKidsResponse?>
}

