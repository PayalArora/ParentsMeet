package com.logicsquare.parentsmeet.model

class ProfileRequest {
    lateinit var email: String
    var name: NameRequest = NameRequest()
    lateinit var phoneCountryCode: String
    lateinit var phone: String
    lateinit var profession: String
    lateinit var address: Address
    var preferences= ParentInterest()
    var dob: DOB = DOB()

class NameRequest {
    lateinit var first: String
    lateinit var last: String
}

class DOB {
    lateinit var day: String
    lateinit var month: String
    lateinit var year: String
}
    class ParentInterest {
        lateinit var parentInterests:ArrayList<String>
    }
}