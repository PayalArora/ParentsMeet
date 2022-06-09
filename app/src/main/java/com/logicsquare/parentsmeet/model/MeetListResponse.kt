package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class MeetListResponse(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
)

data class UsersItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("preferences")
	val preferences: Preferences? = null,

	@field:SerializedName("kidsCount")
	val kidsCount: Int? = null,

	@field:SerializedName("phoneCountryCode")
	val phoneCountryCode: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: Name? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("relation")
	val relation: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("kidObject")
	val kidObject: KidObject? = null,

	@field:SerializedName("profession")
	val profession: String? = ""
)

data class KidObject(

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
	val age: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("genderPronouns")
	val genderPronouns: String? = null
)

