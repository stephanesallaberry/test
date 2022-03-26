package fr.stephanesallaberry.news.android.domain.external

import fr.stephanesallaberry.news.android.domain.external.entity.Breed
import fr.stephanesallaberry.news.android.domain.internal.ICatProvider

class CatInteractor(private val catProvider: ICatProvider) {
    suspend fun getBreeds(): List<Breed> {
        return catProvider.getBreeds()
    }
}
