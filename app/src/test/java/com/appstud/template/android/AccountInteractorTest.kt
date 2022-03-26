package com.appstud.template.android

import com.appstud.template.android.domain.external.AccountInteractor
import com.appstud.template.android.domain.external.entity.AsyncResult
import com.appstud.template.android.domain.external.entity.UserAccount
import com.appstud.template.android.domain.external.entity.UserToken
import com.appstud.template.android.domain.internal.IStorageProvider
import com.appstud.template.android.domain.internal.IUserApiProvider
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class AccountInteractorTest {
    private val userToken = UserToken(
        accessToken = "access",
        refreshToken = "refresh",
        accessTokenExpiration = 0,
        refreshTokenExpiration = 0
    )
    private val userAccount = UserAccount(3, "email@example.com")

    private val fakeStorageProvider = object : IStorageProvider {

        override fun getUserAccount(): UserAccount? = null

        override fun getToken(): UserToken? = null

        override fun clearAll() = Unit

        override fun clearUserAccount() = Unit

        override fun setUserAccount(userInfo: UserAccount) = Unit

        override fun clearUserToken() = Unit

        override fun setToken(userToken: UserToken) = Unit
    }

    private val fakeUserApiProvider = mock(IUserApiProvider::class.java)
    private val spyStorageProvider = spy(fakeStorageProvider)

    @Before
    fun init() {
        runBlockingTest {
            `when`(fakeUserApiProvider.login(anyString(), anyString())).thenReturn(userToken)
            `when`(fakeUserApiProvider.getProfile()).thenReturn(userAccount)
        }
    }

    @Test
    fun `login user returns user profile`() {
        runBlockingTest {
            val accountInteractor = AccountInteractor(fakeStorageProvider, fakeUserApiProvider)
            val user = accountInteractor.login("email@example.com", "password")
            assertEquals(user, AsyncResult.Success(userAccount))
        }
    }

    @Test
    fun `login user saves user token`() {
        runBlockingTest {
            val accountInteractor = AccountInteractor(spyStorageProvider, fakeUserApiProvider)
            val user = accountInteractor.login("email@example.com", "password")
            verify(spyStorageProvider).setToken(userToken)
        }
    }

    @Test
    fun `logout user call clearAll`() {
        runBlockingTest {
            val accountInteractor = AccountInteractor(spyStorageProvider, fakeUserApiProvider)
            accountInteractor.logout()
            verify(spyStorageProvider).clearAll()
        }
    }
}
