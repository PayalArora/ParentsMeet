package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class SettingResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("setting")
	val setting: Setting? = null
) {

	data class Setting(

		@field:SerializedName("createdAt")
		val createdAt: String? = null,

		@field:SerializedName("preferences")
		val preferences: Preferences? = null,

		@field:SerializedName("isCreatedByAdmin")
		val isCreatedByAdmin: Boolean? = null,

		@field:SerializedName("__v")
		val V: Int? = null,

		@field:SerializedName("_id")
		val id: String? = null,

		@field:SerializedName("_createdBy")
		val createdBy: String? = null,

		@field:SerializedName("updatedAt")
		val updatedAt: String? = null
	)

	data class Preferences(

		@field:SerializedName("parentInterests")
		val parentInterests: List<Any?>? = null,

		@field:SerializedName("jobCategories")
		val jobCategories: List<String>? = null,

		@field:SerializedName("activities")
		val activities: List<String?>? = null,

		@field:SerializedName("timings")
		val timings: List<Any?>? = null,

		@field:SerializedName("games")
		val games: List<String?>? = null,

		@field:SerializedName("specialNeeds")
		val specialNeeds: List<Any?>? = null,

		@field:SerializedName("jobTypes")
		val jobTypes: List<String>? = null
	)
}
