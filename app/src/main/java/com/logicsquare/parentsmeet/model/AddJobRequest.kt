package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

class AddJobRequest {

    var limit: Int = 100
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
    @SerializedName("jobType")
    var jobtype: ArrayList<String> = arrayListOf()
    var educationRequirement: ArrayList<String> = arrayListOf()
    var locationPreference: ArrayList<String> = arrayListOf()
    var experienceRequirement: ArrayList<String> = arrayListOf()
    var payRange: PayRange = PayRange()
    var jobCategory: ArrayList<String> = arrayListOf()
   // var isShowBothType = true

}


