package fr.stephanesallaberry.news.android.transport.home

import fr.stephanesallaberry.news.android.domain.external.entity.Breed

data class HomeScreenState(
    val breeds: List<Breed> = emptyList<Breed>(),
    val isLoading: Boolean = false
)

sealed class HomeScreenSideEffect {
    data class Toast(val textResource: Int) : HomeScreenSideEffect()
}
