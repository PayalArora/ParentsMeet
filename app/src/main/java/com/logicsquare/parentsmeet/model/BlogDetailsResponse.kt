package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class BlogDetailsResponse(

	@field:SerializedName("relatedBlogs")
	val relatedBlogs: List<Blog?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("blog")
	val blog: Blog? = null
)

data class Blog(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("websiteLink")
	val websiteLink: String? = null,

	@field:SerializedName("isCreatedByAdmin")
	val isCreatedByAdmin: Boolean? = null,

	@field:SerializedName("coverImage")
	val coverImage: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("_createdBy")
	val createdBy: Any? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
