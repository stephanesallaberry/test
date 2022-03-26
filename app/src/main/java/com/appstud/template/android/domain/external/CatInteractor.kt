package com.appstud.template.android.domain.external

import com.appstud.template.android.domain.external.entity.Breed
import com.appstud.template.android.domain.internal.ICatProvider

class CatInteractor(private val catProvider: ICatProvider) {
    suspend fun getBreeds(): List<Breed> {
        return catProvider.getBreeds()
    }
}
