package com.logicsquare.parentsmeet.model

class SubmitOtpRequest {

    lateinit var handle: String
    lateinit var sendTo: String
    lateinit var otp: String
    var isResetPassword: Boolean = false
    var password: String? = null

}
