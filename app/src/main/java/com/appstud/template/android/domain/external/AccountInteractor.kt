package fr.stephanesallaberry.news.android.domain.external

import fr.stephanesallaberry.news.android.domain.external.entity.AsyncResult
import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.domain.internal.IStorageProvider
import fr.stephanesallaberry.news.android.domain.internal.IUserApiProvider

class AccountInteractor(
    private val storageProvider: IStorageProvider,
    private val userApiProvider: IUserApiProvider
) {
    fun isConnected(): Boolean {
        return storageProvider.getToken() != null
    }

    @SuppressWarnings("TooGenericExceptionCaught") // TODO catch and throw our own errors
    suspend fun login(email: String, password: String): AsyncResult<UserAccount> {
        return try {
            val token = userApiProvider.login(email, password)
            storageProvider.setToken(token)
            val userAccount = userApiProvider.getProfile()
            AsyncResult.Success(userAccount)
        } catch (exception: Exception) {
            AsyncResult.Fail(exception)
        }
    }

    fun logout() {
        storageProvider.clearAll()
    }
}
