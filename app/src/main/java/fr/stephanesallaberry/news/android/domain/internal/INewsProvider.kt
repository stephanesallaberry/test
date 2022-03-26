package fr.stephanesallaberry.news.android.domain.internal

import fr.stephanesallaberry.news.android.domain.external.entity.Article

interface INewsProvider {
    suspend fun getNews(): List<Article>
}
