package fr.stephanesallaberry.news.android.transport.detail

import fr.stephanesallaberry.news.android.domain.external.entity.Breed

data class DetailScreenState(
    val breed: Breed? = null,
    val isLoading: Boolean = false
)

sealed class DetailScreenSideEffect {
    data class Toast(val textResource: Int) : DetailScreenSideEffect()
}
