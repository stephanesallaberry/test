package fr.stephanesallaberry.news.android.domain.external.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: Date
) : Parcelable