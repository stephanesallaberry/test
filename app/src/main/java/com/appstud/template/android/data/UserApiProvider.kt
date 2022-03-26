package com.appstud.template.android.data

import com.appstud.template.android.domain.external.entity.UserAccount
import com.appstud.template.android.domain.external.entity.UserToken
import com.appstud.template.android.domain.internal.IUserApiProvider
import java.util.Calendar
import java.util.GregorianCalendar

class UserApiProvider : IUserApiProvider {
    @SuppressWarnings("MagicNumber") // fake user for now
    override suspend fun getProfile(): UserAccount {
        return UserAccount(3, "email")
    }

    override suspend fun login(email: String, password: String): UserToken {
        val expirationDate = GregorianCalendar()
        expirationDate.add(Calendar.DAY_OF_YEAR, 1)
        val expirationMillis = expirationDate.timeInMillis
        return UserToken("access", "refresh", expirationMillis, expirationMillis)
    }
}
