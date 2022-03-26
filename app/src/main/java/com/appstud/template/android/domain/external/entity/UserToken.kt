package com.appstud.template.android.domain.external.entity

data class UserToken(
    val accessToken: String,
    val refreshToken: String?,
    // timestamp
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long?
)
