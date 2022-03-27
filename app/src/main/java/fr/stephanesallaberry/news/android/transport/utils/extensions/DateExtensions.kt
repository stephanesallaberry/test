package fr.stephanesallaberry.news.android.transport.utils.extensions

import java.text.DateFormat
import java.util.Date

fun Date?.toShortDate(): String {
    this ?: return ""

    val timeFormat = DateFormat.getDateTimeInstance(
        DateFormat.SHORT,
        DateFormat.SHORT
    )

    return timeFormat.format(this)
}
