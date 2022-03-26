package com.appstud.template.android.data.network

import com.appstud.template.android.domain.external.entity.Breed
import retrofit2.http.GET

interface CatApi {
    @GET("breeds")
    suspend fun getBreeds(): List<Breed>
}
