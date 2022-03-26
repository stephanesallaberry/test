package fr.stephanesallaberry.news.android.domain.internal

import fr.stephanesallaberry.news.android.domain.external.entity.Breed

interface ICatProvider {
    suspend fun getBreeds(): List<Breed>
}
