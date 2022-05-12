package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class AddCommentResponse(

	@field:SerializedName("comment")
	val comment: CommentsItem? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)
