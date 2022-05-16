package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class GetAllCommentsResponse(

	@field:SerializedName("comments")
	var comments: List<CommentsItem?>? = null,

	@field:SerializedName("count")
	var count: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
) {

	data class User(

		@field:SerializedName("name")
		val name: Name? = null,

		@field:SerializedName("_id")
		val id: String? = null,

		@field:SerializedName("userType")
		val userType: String? = null,

		@field:SerializedName("id")
		val _id: String? = null
	)


	data class Name(

		@field:SerializedName("last")
		val last: String? = null,

		@field:SerializedName("first")
		val first: String? = null,

		@field:SerializedName("full")
		val full: String? = null
	)
}
