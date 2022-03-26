package com.appstud.template.android.transport.detail

import com.appstud.template.android.domain.external.entity.Breed

data class DetailScreenState(
    val breed: Breed? = null,
    val isLoading: Boolean = false
)

sealed class DetailScreenSideEffect {
    data class Toast(val textResource: Int) : DetailScreenSideEffect()
}
