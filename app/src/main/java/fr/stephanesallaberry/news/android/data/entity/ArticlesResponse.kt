package fr.stephanesallaberry.news.android.data.entity

import fr.stephanesallaberry.news.android.domain.external.entity.Article

data class ArticlesResponse(
    val articles: List<Article>
)
