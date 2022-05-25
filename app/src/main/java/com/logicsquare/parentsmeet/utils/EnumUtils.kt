package com.logicsquare.parentsmeet.utils

class EnumUtils {
    enum class locationPreference(val location:String){
        Remote("Remote"), InPerson("In Person"), Hybrid("Hybrid")
    }
    enum class educationRequirement(val eduction:String){
        AllEducationLevels("All Education Levels"), HighSchoolDegree("High School Degree"), AssociateDegree("Associate's Degree")
        , BachelorDegree("BachelorDegree"),MasterDegree("Master's Degree")
    }
    enum class experienceRequirement(val degree:String){
        AllExperience("All Experience"), EntryLevel("Entry Level"), MidLevel("Mid Level")
        , SeniorLevel("Senior Level")
    }
    enum class jobType(val type:String){
        FullTime("Full Time"), Temporary("Temporary"), Contract("Contract")
    }
}
