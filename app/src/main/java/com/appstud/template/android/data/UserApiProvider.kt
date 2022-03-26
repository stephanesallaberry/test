package fr.stephanesallaberry.news.android.data

import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.domain.external.entity.UserToken
import fr.stephanesallaberry.news.android.domain.internal.IUserApiProvider
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
