package fr.stephanesallaberry.news.android.transport.home

import fr.stephanesallaberry.news.android.domain.external.entity.Article

data class HomeScreenState(
    val articles: List<Article> = emptyList<Article>(),
    val isLoading: Boolean = false
)

sealed class HomeScreenSideEffect {
    data class Toast(val textResource: Int) : HomeScreenSideEffect()
}
