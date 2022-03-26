package com.appstud.template.android.transport.home

import com.appstud.template.android.domain.external.entity.Breed

data class HomeScreenState(
    val breeds: List<Breed> = emptyList<Breed>(),
    val isLoading: Boolean = false
)

sealed class HomeScreenSideEffect {
    data class Toast(val textResource: Int) : HomeScreenSideEffect()
}
