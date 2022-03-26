package fr.stephanesallaberry.news.android.transport

data class MainScreenState(
    val isLoading: Boolean = true,
    val isConnected: Boolean = false
)

sealed class MainScreenSideEffect {
    data class Toast(val textResource: Int) : MainScreenSideEffect()
}
