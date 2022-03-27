package fr.stephanesallaberry.news.android.transport.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.browse(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    this.startActivity(i)
}
