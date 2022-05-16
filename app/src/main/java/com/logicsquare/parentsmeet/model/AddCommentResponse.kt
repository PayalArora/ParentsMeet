package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class AddCommentResponse(

	@field:SerializedName("comment")
	val comment: CommentsItem? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,
	@field:SerializedName("forum")
	val forum: Forum? = null,

)
data class Forum(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("comments")
	val comments: Int? = null,

	@field:SerializedName("isCreatedByAdmin")
	val isCreatedByAdmin: Boolean? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("_createdBy")
	val createdBy: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("likes")
	val likes: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
