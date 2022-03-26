package com.appstud.template.android

import com.appstud.template.android.data.network.TokenInterceptor
import com.appstud.template.android.domain.external.entity.UserToken
import com.appstud.template.android.domain.internal.IStorageProvider
import com.google.gson.Gson
import java.util.Calendar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class TokenInterceptorTest {

    private val storageProvider = mock(IStorageProvider::class.java)

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    private lateinit var sessionToken: UserToken

    /**
     * Nominal case:
     * We have a valid token, and we want to make a request.
     * The interceptor should add the token to the request's header.
     */
    @Test
    fun interceptorAddAccessTokenTest() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        val dateExpirationToken = cal.timeInMillis
        val userToken = UserToken(
            "accessToken",
            "refreshToken",
            dateExpirationToken,
            dateExpirationToken
        )

        `when`(storageProvider.getToken()).thenReturn(userToken)

        `when`(storageProvider.setToken(any())).thenAnswer { invocation ->
            sessionToken = invocation.arguments[0] as UserToken
            true
        }

        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        mockWebServer.start()
        val mockUrl =
            mockWebServer.url("/api/test") // request to be made just to test the header addition

        val authServer = MockWebServer()
        authServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                Gson().toJson(
                    UserToken(
                        "accessToken",
                        "refreshToken",
                        dateExpirationToken,
                        dateExpirationToken
                    )
                )
            )
        )
        authServer.start()
        val authUrl = authServer.url("/")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(storageProvider, authUrl.toString())).build()
        val request = Request.Builder().url(mockUrl.toString()).build()

        val response = okHttpClient.newCall(request).execute()

        assertEquals("Bearer ${userToken.accessToken}", response.request.header("Authorization"))

        mockWebServer.shutdown()
    }

    /**
     * We have an expired access token with a valid refresh token, and we want to make a request.
     * The interceptor should first get a new access token via /refresh web service,
     * and then add the token to the request's header.
     */
    @SuppressWarnings("LongMethod")
    @Test
    fun interceptorRefreshAccessTokenTest() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, -1)
        val dateExpirationToken = cal.timeInMillis
        cal.add(Calendar.HOUR_OF_DAY, 3)
        val dateExpirationRefreshToken = cal.timeInMillis

        val userToken = UserToken(
            "accessToken",
            "refreshToken",
            dateExpirationToken,
            dateExpirationRefreshToken
        )

        `when`(storageProvider.getToken()).thenReturn(userToken)

        `when`(storageProvider.setToken(any())).thenAnswer { invocation ->
            sessionToken = invocation.arguments[0] as UserToken
            true
        }

        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        mockWebServer.enqueue(MockResponse().setResponseCode(401))
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        mockWebServer.start()
        val mockUrl =
            mockWebServer.url("/api/test") // request to be made just to test the header addition

        val authServer = MockWebServer()
        val userToken2 = UserToken(
            "accessToken2",
            "refreshToken",
            dateExpirationRefreshToken,
            dateExpirationRefreshToken
        )
        authServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                Gson().toJson(
                    userToken2
                )
            )
        )

        authServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                Gson().toJson(
                    UserToken(
                        "accessToken3",
                        "refreshToken",
                        dateExpirationRefreshToken,
                        dateExpirationRefreshToken
                    )
                )
            )
        )
        authServer.start()
        val authUrl = authServer.url("/")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(storageProvider, authUrl.toString())).build()
        val request = Request.Builder().url(mockUrl.toString()).build()

        val response = okHttpClient.newCall(request).execute()
        /* 1st request, where our token is expired -> refresh and get a new one*/
        assertEquals("Bearer accessToken2", response.request.header("Authorization"))

        /* 2nd request, where our token is good but we get a 401 -> refresh and get a new one*/
        val request2 = Request.Builder().url(mockUrl.toString()).build()
        val response2 = okHttpClient.newCall(request2).execute()
        assertEquals("Bearer accessToken3", response2.request.header("Authorization"))

        mockWebServer.shutdown()
    }

    /**
     * We have no token at all, and we want to make a request.
     * The interceptor should do nothing, just complete the initial request.
     */
    @Test
    fun interceptorNoAccessTokenTest() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        val dateExpirationToken = cal.timeInMillis
        `when`(storageProvider.getToken()).thenReturn(null)

        `when`(storageProvider.setToken(any())).thenAnswer { invocation ->
            sessionToken = invocation.arguments[0] as UserToken
            true
        }

        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        mockWebServer.start()
        val mockUrl =
            mockWebServer.url("/api/test") // request to be made just to test the header addition

        val authServer = MockWebServer()
        authServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                Gson().toJson(
                    UserToken(
                        "accessToken",
                        "refreshToken",
                        dateExpirationToken,
                        dateExpirationToken
                    )
                )
            )
        )
        authServer.start()
        val authUrl = authServer.url("/")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(storageProvider, authUrl.toString())).build()
        val request = Request.Builder().url(mockUrl.toString()).build()

        val response = okHttpClient.newCall(request).execute()
        assertNull(response.request.header("Authorization"))

        mockWebServer.shutdown()
    }

    /**
     * We have an expired access token and an expired refresh token, and we want to make a request.
     * The interceptor should do nothing, just complete the initial request.
     */
    @Test
    fun interceptorAllTokenExpiredTest() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, -1)
        val dateExpirationToken = cal.timeInMillis
        val userToken = UserToken(
            "accessToken",
            "refreshToken",
            dateExpirationToken,
            dateExpirationToken
        )

        `when`(storageProvider.getToken()).thenReturn(userToken)

        `when`(storageProvider.setToken(any())).thenAnswer { invocation ->
            sessionToken = invocation.arguments[0] as UserToken
            true
        }

        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        mockWebServer.start()
        val mockUrl =
            mockWebServer.url("/api/test") // request to be made just to test the header addition

        val authServer = MockWebServer()
        authServer.enqueue(
            MockResponse().setResponseCode(200).setBody(
                Gson().toJson(
                    UserToken(
                        "accessToken",
                        "refreshToken",
                        dateExpirationToken,
                        dateExpirationToken
                    )
                )
            )
        )
        authServer.start()
        val authUrl = authServer.url("/")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(storageProvider, authUrl.toString())).build()
        val request = Request.Builder().url(mockUrl.toString()).build()

        val response = okHttpClient.newCall(request).execute()
        assertNull(response.request.header("Authorization"))

        mockWebServer.shutdown()
    }
}
