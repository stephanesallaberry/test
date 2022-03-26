package com.appstud.template.android.data

import com.appstud.template.android.data.network.CatApi
import com.appstud.template.android.domain.external.entity.Breed
import com.appstud.template.android.domain.internal.ICatProvider
import retrofit2.HttpException
import timber.log.Timber

class CatProvider(private val catApi: CatApi) : ICatProvider {
    override suspend fun getBreeds(): List<Breed> {
        return try {
            catApi.getBreeds()
        } catch (exception: HttpException) {
            // TODO catch and throw errors in case of missing internet connection or others
            Timber.e(exception)
            emptyList()
        }
    }
}
