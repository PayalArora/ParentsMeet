package com.logicsquare.parentsmeet.model

import com.google.gson.annotations.SerializedName

data class CommentsItem(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("_likes")
    val likes: List<Any?>? = null,

    @field:SerializedName("isDeleted")
    val isDeleted: Boolean? = null,

    @field:SerializedName("__v")
    val V: Int? = null,

    @field:SerializedName("isSubComment")
    val isSubComment: Boolean? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("_user")
    val user: GetAllCommentsResponse.User? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("_forum")
    val forum: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)
