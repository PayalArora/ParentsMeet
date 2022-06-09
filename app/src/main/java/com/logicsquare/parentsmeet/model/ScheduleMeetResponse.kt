package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class ScheduleMeetResponse(

	@field:SerializedName("meet")
	val meet: Meet? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class ToMatchedKid(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
)

data class To(

	@field:SerializedName("name")
	val name: Name? = null,

	@field:SerializedName("_id")
	val _id: String? = null,

	@field:SerializedName("userType")
	val userType: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Meet(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("_from")
	val from: String? = null,

	@field:SerializedName("comments")
	val comments: List<Any?>? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("_toMatchedKid")
	val toMatchedKid: ToMatchedKid? = null,

	@field:SerializedName("_to")
	val to: To? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("_fromMatchedKid")
	val fromMatchedKid: FromMatchedKid? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class FromMatchedKid(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
)
