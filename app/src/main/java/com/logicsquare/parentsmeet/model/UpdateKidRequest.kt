package com.logicsquare.parentsmeet.model

class UpdateKidRequest {
    lateinit var name: String
    lateinit var age: String
    lateinit var genderPronouns: String
    lateinit var grade: String
    var colorBar: String = "Yellow"
    var  preferences= Preferences()

    class Preferences{
        lateinit var games:ArrayList<String>
        lateinit var specialNeeds:ArrayList<String>
        lateinit var activities:ArrayList<String>

    }
}
