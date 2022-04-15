package com.logicsquare.parentsmeet.network

data class AccessTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val id_token: String,
    val token_type: String
)