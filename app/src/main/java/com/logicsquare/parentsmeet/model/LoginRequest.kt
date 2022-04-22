
package com.logicsquare.parentsmeet.model
class LoginRequest {
    lateinit var handle: String
    lateinit var password: String
    var deviceDetails: DeviceDetails = DeviceDetails()
    var allowNotification: Boolean = false
}

