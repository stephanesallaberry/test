package fr.stephanesallaberry.news.android.data

import fr.stephanesallaberry.news.android.data.network.NewsApi
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import fr.stephanesallaberry.news.android.domain.internal.INewsProvider
import retrofit2.HttpException
import timber.log.Timber

class NewsProvider(private val newsApi: NewsApi) : INewsProvider {
    override suspend fun getNews(): List<Article> {
        return try {
            newsApi.getHeadlines().articles
        } catch (exception: HttpException) {
            // TODO catch and throw errors in case of missing internet connection or others
            Timber.e(exception)
            emptyList()
        }
    }
}
