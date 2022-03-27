package fr.stephanesallaberry.news.android.transport.detail

import fr.stephanesallaberry.news.android.domain.external.entity.Article

data class DetailScreenState(
    val article: Article? = null,
    val isLoading: Boolean = false
)

sealed class DetailScreenSideEffect {
    data class Toast(val textResource: Int) : DetailScreenSideEffect()
}
