package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class LikeForumResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("_isLiked")
	val isLiked: Int? = null,

	@field:SerializedName("likes")
val likes: Int? = null
)
