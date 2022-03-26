package fr.stephanesallaberry.news.android.domain.external

import fr.stephanesallaberry.news.android.domain.external.entity.Article
import fr.stephanesallaberry.news.android.domain.internal.INewsProvider

class NewsInteractor(private val newsProvider: INewsProvider) {
    suspend fun getNews(): List<Article> {
        return newsProvider.getNews()
    }
}
