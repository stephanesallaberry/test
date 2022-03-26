package com.appstud.template.android.data.network

import com.appstud.template.android.data.entity.AuthRequest
import com.appstud.template.android.data.entity.AuthResponse
import com.appstud.template.android.data.entity.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth")
    suspend fun login(@Body credentials: AuthRequest): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body credentials: RefreshRequest): AuthResponse
}
