package com.appstud.template.android.domain.internal

import com.appstud.template.android.domain.external.entity.UserAccount
import com.appstud.template.android.domain.external.entity.UserToken

interface IStorageProvider {
    fun getToken(): UserToken?
    fun clearAll()
    fun clearUserAccount()
    fun getUserAccount(): UserAccount?
    fun setUserAccount(userInfo: UserAccount)
    fun clearUserToken()
    fun setToken(userToken: UserToken)
}
