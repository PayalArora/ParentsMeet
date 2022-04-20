package com.logicsquare.parentsmeet.model

class SignUpRequest {
    lateinit var email: String
    var name: NameRequest = NameRequest()
    lateinit var phoneCountryCode: String
    lateinit var phone: String
    lateinit var password: String
    lateinit var relation: String
    var dob: DOB = DOB()
    var deviceDetails: DeviceDetails = DeviceDetails()
    var allowNotification: Boolean = false
}

class NameRequest {
    lateinit var first: String
    lateinit var last: String
}

class DOB {
    lateinit var day: String
    lateinit var month: String
    lateinit var year: String
}

class DeviceDetails {
    lateinit var name: String
    lateinit var deviceId: String
}