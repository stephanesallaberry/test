package com.appstud.template.android.domain.internal

import com.appstud.template.android.domain.external.entity.Breed

interface ICatProvider {
    suspend fun getBreeds(): List<Breed>
}
