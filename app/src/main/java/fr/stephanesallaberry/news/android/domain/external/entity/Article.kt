package fr.stephanesallaberry.news.android.domain.external.entity

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val content: String,
    val urlToImage: String,
    val url: String,
    val publishedAt: Date
) : Parcelable
