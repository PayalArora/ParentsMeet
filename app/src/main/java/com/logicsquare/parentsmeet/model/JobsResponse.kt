package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class JobsResponse(

	@field:SerializedName("jobs")
	val jobs: List<JobsItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class JobsItem(

	@field:SerializedName("experienceRequirement")
	val experienceRequirement: String? = null,

	@field:SerializedName("jobCategory")
	val jobCategory: String? = null,

	@field:SerializedName("applyJobLink")
	val applyJobLink: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("_createdBy")
	val createdBy: CreatedBy? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("_isJobSaved")
	var isJobSaved: Int? = null,

	@field:SerializedName("_savedByUser")
	val savedByUser: List<Any?>? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("jobType")
	val jobType: String? = null,

	@field:SerializedName("payRange")
	val payRange: PayRange? = null,

	@field:SerializedName("educationRequirement")
	val educationRequirement: String? = null,

	@field:SerializedName("_appliedByUser")
	val appliedByUser: List<Any?>? = null,

	@field:SerializedName("jobSavedCount")
	val jobSavedCount: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("locationPreference")
	val locationPreference: String? = null,

	@field:SerializedName("isCreatedByAdmin")
	val isCreatedByAdmin: Boolean? = null,

	@field:SerializedName("jobFor")
	val jobFor: String? = null,

	@field:SerializedName("_isJobApplied")
	var isJobApplied: Int? = null,

	@field:SerializedName("_id")
	val jobId: String? = null,

	@field:SerializedName("jobAppliedCount")
	val jobAppliedCount: Int? = null
)


data class JobsName(

	@field:SerializedName("last")
	val last: String? = null,

	@field:SerializedName("first")
	val first: String? = null,

	@field:SerializedName("full")
	val full: String? = null
)

data class CreatedBy(

	@field:SerializedName("name")
	val name: JobsName? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("userType")
	val userType: String? = null,

	@field:SerializedName("id")
	val jobId: String? = null
)

data class Address(

	@field:SerializedName("zip")
	val zip: String? = null
)
