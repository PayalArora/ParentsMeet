package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class ForumDetailResponse(

	@field:SerializedName("forum")
	val forum: Forum? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
) {


	data class CreatedBy(

		@field:SerializedName("name")
		val name: Name? = null,

		@field:SerializedName("_id")
		val _id: String? = null,

		@field:SerializedName("userType")
		val userType: String? = null,

		@field:SerializedName("id")
		val id: String? = null
	)

	data class Name(

		@field:SerializedName("last")
		val last: String? = null,

		@field:SerializedName("first")
		val first: String? = null,

		@field:SerializedName("full")
		val full: String? = null
	)

	data class Forum(

		@field:SerializedName("comments")
		val comments: Int? = null,

		@field:SerializedName("isCreatedByAdmin")
		val isCreatedByAdmin: Boolean? = null,

		@field:SerializedName("description")
		val description: String? = null,

		@field:SerializedName("_createdBy")
		val createdBy: CreatedBy? = null,

		@field:SerializedName("title")
		val title: String? = null,

		@field:SerializedName("type")
		val type: String? = null,

		@field:SerializedName("isActive")
		val isActive: Boolean? = null,

		@field:SerializedName("createdAt")
		val createdAt: String? = null,

		@field:SerializedName("__v")
		val V: Int? = null,

		@field:SerializedName("_id")
		val id: String? = null,

		@field:SerializedName("likes")
		val likes: Int? = null,

		@field:SerializedName("updatedAt")
		val updatedAt: String? = null,

		@field:SerializedName("_isLiked")
		val isLiked: Int? = null
	)
}
