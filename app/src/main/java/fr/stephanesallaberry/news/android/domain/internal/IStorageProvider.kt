package fr.stephanesallaberry.news.android.domain.internal

import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.domain.external.entity.UserToken

interface IStorageProvider {
    fun getToken(): UserToken?
    fun clearAll()
    fun clearUserAccount()
    fun getUserAccount(): UserAccount?
    fun setUserAccount(userInfo: UserAccount)
    fun clearUserToken()
    fun setToken(userToken: UserToken)
}
