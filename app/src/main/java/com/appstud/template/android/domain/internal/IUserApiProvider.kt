package fr.stephanesallaberry.news.android.domain.internal

import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.domain.external.entity.UserToken

interface IUserApiProvider {
    suspend fun login(email: String, password: String): UserToken
    suspend fun getProfile(): UserAccount
}
