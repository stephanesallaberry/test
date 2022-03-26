package fr.stephanesallaberry.news.android.data.network

import fr.stephanesallaberry.news.android.data.entity.AuthRequest
import fr.stephanesallaberry.news.android.data.entity.AuthResponse
import fr.stephanesallaberry.news.android.data.entity.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth")
    suspend fun login(@Body credentials: AuthRequest): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body credentials: RefreshRequest): AuthResponse
}
