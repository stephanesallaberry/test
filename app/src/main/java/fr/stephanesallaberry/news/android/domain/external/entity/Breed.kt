package fr.stephanesallaberry.news.android.domain.external.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(
    val name: String,
    val description: String,
    val image: NetworkImage
) : Parcelable
