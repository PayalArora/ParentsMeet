package com.logicsquare.parentsmeet.model

class AddJobRequest {

    var limit: Int = 10
    var filters: Filters = Filters()


}

enum class JOBTYPE(val jobtype: String) {
    ALL("all"), JOBAPPLIED("jobApplied"), JOBSAVED("jobSaved")
}
class PayRange {
    var min: Int = 500000
}
class Filters {
    lateinit var category: String
    var jobtype: ArrayList<String> = arrayListOf()
    var educationRequirement: ArrayList<String> = arrayListOf()
    var locationPreference: ArrayList<String> = arrayListOf()
    var experienceRequirement: ArrayList<String> = arrayListOf()
    lateinit  var payRange: PayRange
    var jobCategory: ArrayList<String> = arrayListOf()
}


