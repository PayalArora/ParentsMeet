package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

class MeetRequest {
    var filters: Filters = Filters()
    var limit: Int = 100

    class Filters {
        @field:SerializedName("preferences")
        var preferences: AddKidsPreferences = AddKidsPreferences()
        var age :Age = Age()
        var location :Location = Location()

        data class Age(
            var min: Int = 4,
            var max: Int=  5,
        )

        data class Location(
            var lat: Double? = null,
            var lng: Double? = null,
            var miles: Int? = null,
        )
    }
}