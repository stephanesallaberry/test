package fr.stephanesallaberry.news.android.domain.external.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val urlToImage: String
) : Parcelable
