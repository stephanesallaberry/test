package fr.stephanesallaberry.news.android.data.network

import fr.stephanesallaberry.news.android.domain.external.entity.Breed
import retrofit2.http.GET

interface CatApi {
    @GET("breeds")
    suspend fun getBreeds(): List<Breed>
}
