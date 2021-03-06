package fr.stephanesallaberry.news.android.data.entity

data class AuthResponse(
    val accessToken: String,
    val accessTokenExpiration: Long,
    val refreshToken: String?,
    val refreshTokenExpiration: Long?
)
