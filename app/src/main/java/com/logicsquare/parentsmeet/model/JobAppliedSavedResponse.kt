package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class JobAppliedSavedResponse (

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("_isJobApplied")
    val isJobApplied: Int? = null,
    @field:SerializedName("_isJobSaved")
    val isJobSaved: Int? = null

)