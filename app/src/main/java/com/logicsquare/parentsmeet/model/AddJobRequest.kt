package com.logicsquare.parentsmeet.model

class AddJobRequest {

    var limit: Int = 10
    var filters : Filters = Filters()
}

enum class JOBTYPE(val jobtype: String){
    ALL("all"), JOBAPPLIED("jobApplied"), JOBSAVED("jobSaved")
}

class Filters{
    lateinit var category: String
}