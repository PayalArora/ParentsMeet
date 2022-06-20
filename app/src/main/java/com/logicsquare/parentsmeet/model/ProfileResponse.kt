package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,
	@field:SerializedName("user")
	val user: ProfileUser? = null
){
	data class Address(

		@field:SerializedName("zip")
		val zip: String? = null,

		@field:SerializedName("country")
		val country: String? = null,

		@field:SerializedName("city")
		val city: String? = null,

		@field:SerializedName("street")
		val street: String? = null,

		@field:SerializedName("timeZone")
		val timeZone: String? = null,

		@field:SerializedName("state")
		val state: String? = null
	)
}

data class UserDob(

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
)

data class KidsItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("preferences")
	val preferences: Preferences? = null,

	@field:SerializedName("_parent")
	val parent: String? = null,

	@field:SerializedName("colorBar")
	val colorBar: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("genderPronouns")
    val genderPronouns: String? = null

)

data class ProfileUser(

	@field:SerializedName("_jobSaved")
	val jobSaved: List<Any?>? = null,

	@field:SerializedName("_jobApplied")
	val jobApplied: List<Any?>? = null,

	@field:SerializedName("deviceTokens")
	val deviceTokens: List<Any?>? = null,

	@field:SerializedName("_meet")
	val meet: List<Any?>? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("isEmailVerified")
	val isEmailVerified: Boolean? = null,

	@field:SerializedName("relation")
	val relation: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("id")
	val profileUserId: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("jobSavedCount")
	val jobSavedCount: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("_kids")
	val kids: List<KidsItem?>? = null,

	@field:SerializedName("preferences")
	val preferences: Preferences? = null,

	@field:SerializedName("isPhoneVerified")
	val isPhoneVerified: Boolean? = null,

	@field:SerializedName("kidsCount")
	val kidsCount: Int? = null,

	@field:SerializedName("meetCount")
	val meetCount: Int? = null,

	@field:SerializedName("phoneCountryCode")
	val phoneCountryCode: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("dob")
	val dob: UserDob? = null,

	@field:SerializedName("name")
	val name: ProfileName? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("userType")
	val userType: String? = null,

	@field:SerializedName("jobAppliedCount")
	val jobAppliedCount: Int? = null,

	@field:SerializedName("username")
	val username: String? = null	,

	@field:SerializedName("profession")
	val profession: String? = null
)

data class Preferences(

	@field:SerializedName("parentInterests")
	val parentInterests: List<String>? = null,

	@field:SerializedName("activities")
	val activities: List<String>? = null,

	@field:SerializedName("games")
	val games: List<String>? = null,

	@field:SerializedName("timings")
	val timings: List<String>? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<Any?>? = null
)

data class Location(

	@field:SerializedName("coordinates")
	val coordinates: List<String?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class ProfileName(

	@field:SerializedName("last")
	val last: String? = null,

	@field:SerializedName("first")
	val first: String? = null,

	@field:SerializedName("full")
	val full: String? = null
)
