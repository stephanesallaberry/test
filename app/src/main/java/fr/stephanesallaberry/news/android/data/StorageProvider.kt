package fr.stephanesallaberry.news.android.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.stephanesallaberry.news.android.BuildConfig
import fr.stephanesallaberry.news.android.domain.external.entity.UserAccount
import fr.stephanesallaberry.news.android.domain.external.entity.UserToken
import fr.stephanesallaberry.news.android.domain.internal.IStorageProvider

private const val SHARED_PREFERENCES = BuildConfig.APPLICATION_ID
private const val SHARED_PREFERENCES_TOKEN = "session:token"
private const val SHARED_PREFERENCES_USER_ACCOUNT = "user:connected"

class StorageProvider(context: Context) : IStorageProvider {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }
    private val gson: Gson = Gson()

    override fun setToken(userToken: UserToken) {
        return set(SHARED_PREFERENCES_TOKEN, userToken)
    }

    override fun clearUserToken() {
        return sharedPreferences.edit().remove(SHARED_PREFERENCES_TOKEN).apply()
    }

    override fun setUserAccount(userInfo: UserAccount) {
        return set(SHARED_PREFERENCES_USER_ACCOUNT, userInfo)
    }

    override fun getUserAccount(): UserAccount? {
        return get(SHARED_PREFERENCES_USER_ACCOUNT)
    }

    override fun clearUserAccount() {
        return sharedPreferences.edit().remove(SHARED_PREFERENCES_USER_ACCOUNT).apply()
    }

    override fun clearAll() {
        clearUserAccount()
        clearUserToken()
    }

    override fun getToken(): UserToken? {
        return get(SHARED_PREFERENCES_TOKEN)
    }

    private inline fun <reified T> get(nameInDB: String): T {
        return gson.fromJson(
            sharedPreferences.getString(nameInDB, null),
            genericType<T>()
        )
    }

    private inline fun <reified T> set(nameInDB: String, objectToSave: T?) {
        return sharedPreferences.edit().putString(nameInDB, gson.toJson(objectToSave)).apply()
    }

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
}
