package fr.stephanesallaberry.news.android.data.network

import fr.stephanesallaberry.news.android.data.entity.ArticlesResponse
import retrofit2.http.GET

interface NewsApi {
    @GET("top-headlines?sources=techcrunch")
    suspend fun getHeadlines(): ArticlesResponse
}
