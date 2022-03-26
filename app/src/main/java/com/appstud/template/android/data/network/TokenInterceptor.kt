package com.appstud.template.android.data.network

import com.appstud.template.android.BuildConfig
import com.appstud.template.android.data.entity.AuthResponse
import com.appstud.template.android.data.entity.RefreshRequest
import com.appstud.template.android.domain.external.entity.UserToken
import com.appstud.template.android.domain.internal.IStorageProvider
import java.net.HttpURLConnection
import java.util.Date
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class TokenInterceptor(
    private val storageProvider: IStorageProvider,
    private val apiBaseURL: String
) :
    Interceptor {
    private var authApi: AuthApi? = null

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = storageProvider.getToken()

        return when {
            token == null -> chain.proceed(originalRequest) // not connected, let it go
            originalRequest.url.encodedPath.endsWith("/auth") ->
                chain.proceed(originalRequest) // login request, don't add token in header!
            isTokenExpired(token.accessTokenExpiration) ->
                refreshTokenAndRetry(chain, originalRequest)
            else -> addTokenAndTry(originalRequest, token, chain)
        }
    }

    private fun addTokenAndTry(
        originalRequest: Request,
        token: UserToken,
        chain: Interceptor.Chain
    ): Response {
        // add token and execute the request
        val updateRequest =
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer " + token.accessToken).build()
        val response = chain.proceed(updateRequest)

        // if an authentication error refresh the token and replay the request
        return if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            response.close()
            refreshTokenAndRetry(chain, updateRequest)
        } else {
            response
        }
    }

    private fun isTokenExpired(tokenDate: Long): Boolean {
        return Date(tokenDate).before(Date())
    }

    private fun refreshTokenAndRetry(chain: Interceptor.Chain, originalRequest: Request): Response {
        val token = refreshToken()
        val updateRequestBuilder =
            originalRequest.newBuilder()

        if (token != null)
            updateRequestBuilder.addHeader(
                "Authorization", "Bearer " + token.accessToken
            )

        // if token null, refresh failed => we could return an error before doing any request,
        // but we don't know what is this request
        // maybe it's legit even if refresh failed

        return chain.proceed(updateRequestBuilder.build())
    }

    /* TooGenericExceptionCaught error from detekt must be ignored because
        blockingGet can throw RuntimeException and Error, and there is no multicatch on Kotlin
        language for now */
    @Suppress("TooGenericExceptionCaught")
    private fun refreshToken(): UserToken? {
        val currentToken = storageProvider.getToken()

        if (currentToken?.refreshToken == null ||
            currentToken.refreshTokenExpiration == null ||
            isTokenExpired(currentToken.refreshTokenExpiration)
        )
            return null

        if (this.authApi == null)
            this.authApi = createAuthApi()

        var newToken: AuthResponse?
        try {
            runBlocking {
                newToken = authApi?.refresh(
                    RefreshRequest(
                        currentToken.accessToken,
                        currentToken.refreshToken
                    )
                )
            }
        } catch (e: Exception) {
            newToken = null
        }

        var userToken: UserToken? = null
        newToken?.let { newTokenNotNull ->
            userToken = UserToken(
                newTokenNotNull.accessToken,
                newTokenNotNull.refreshToken,
                newTokenNotNull.accessTokenExpiration,
                newTokenNotNull.refreshTokenExpiration
            )
            storageProvider.setToken(userToken!!)
        }
        return userToken
    }

    @Suppress("MagicNumber")
    private fun createAuthApi(): AuthApi {
        val okHttpClientBuilder = OkHttpClient.Builder().dispatcher(
            Dispatcher(
                ThreadPoolExecutor(
                    0, Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS, SynchronousQueue<Runnable>()
                )
            )

        )
            .addInterceptor {
                val currentToken = storageProvider.getToken()
                it.proceed(
                    it.request().newBuilder().addHeader(
                        "Authorization",
                        "Bearer " + currentToken?.accessToken
                    ).addHeader("Content-Type", "application/json")
                        .build()
                )
            }
        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor.Logger { message -> Timber.d(message) }

            okHttpClientBuilder.addInterceptor(
                HttpLoggingInterceptor(logger).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }

        val okHttpClient = okHttpClientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(apiBaseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(AuthApi::class.java)
    }
}
