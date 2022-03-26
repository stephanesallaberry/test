package fr.stephanesallaberry.news.android.data.entity

data class RefreshRequest(
    val accessToken: String,
    val refreshToken: String,
    val rememberMe: Boolean = true
)
