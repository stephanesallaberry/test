package fr.stephanesallaberry.news.android.data

import fr.stephanesallaberry.news.android.data.network.CatApi
import fr.stephanesallaberry.news.android.domain.external.entity.Breed
import fr.stephanesallaberry.news.android.domain.internal.ICatProvider
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
