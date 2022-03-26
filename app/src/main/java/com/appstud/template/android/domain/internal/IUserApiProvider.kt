package com.appstud.template.android.domain.internal

import com.appstud.template.android.domain.external.entity.UserAccount
import com.appstud.template.android.domain.external.entity.UserToken

interface IUserApiProvider {
    suspend fun login(email: String, password: String): UserToken
    suspend fun getProfile(): UserAccount
}
