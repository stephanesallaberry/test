package fr.stephanesallaberry.news.android.transport.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import fr.stephanesallaberry.news.android.transport.utils.BaseAdapter
import java.text.DateFormat
import java.util.Date

class ArticlesAdapter(
    var list: List<Article?>,
    onItemClicked: (Article?) -> Unit
) :
    BaseAdapter<Article?>(
        mData = list,
        itemLayoutId = R.layout.article_item,
        onClickListener = onItemClicked
    ) {

    init {
        onBinding = { itemView: View, item: Article?, _ ->
            itemView.findViewById<TextView>(R.id.itemTitle).text = item?.title ?: ""
            itemView.findViewById<TextView>(R.id.itemDate).text = item?.publishedAt?.toShortDate()
            itemView.findViewById<View>(R.id.itemImageContainer).clipToOutline = true
            val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
            item?.urlToImage?.let { imageURL ->
                Glide.with(itemView)
                    .load(imageURL)
                    .into(imageView)
            }
        }
    }
}

private fun Date?.toShortDate(): String {
    this ?: return ""

    val timeFormat = DateFormat.getDateTimeInstance(
        DateFormat.SHORT,
        DateFormat.SHORT
    )

    return timeFormat.format(this)
}
