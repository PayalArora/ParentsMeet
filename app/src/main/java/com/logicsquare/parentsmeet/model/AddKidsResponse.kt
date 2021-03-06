package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class AddKidsResponse(

	@field:SerializedName("kid")
	val kid: Kid? = null,

	@field:SerializedName("error")
	val error: Boolean = false,

	@field:SerializedName("reason")
	val reason: String? = null
)

data class AddKidsPreferences(

	@field:SerializedName("activities")
	var activities: List<Any?>? = null,

	@field:SerializedName("games")
	val games: List<Any?>? = null,

	@field:SerializedName("timings")
	var timings: List<Any?>? = null,

	@field:SerializedName("specialNeeds")
	val specialNeeds: List<Any?>? = null,
)

data class Kid(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("preferences")
	val preferences: AddKidsPreferences? = null,

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
	val age: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
