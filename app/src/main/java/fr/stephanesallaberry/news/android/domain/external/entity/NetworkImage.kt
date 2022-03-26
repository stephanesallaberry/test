package fr.stephanesallaberry.news.android.domain.external.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetworkImage(
    val id: String,
    val url: String
) : Parcelable
