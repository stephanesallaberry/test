package fr.stephanesallaberry.news.android.transport.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import fr.stephanesallaberry.news.android.transport.utils.BaseAdapter

class ArticlesAdapter(
    private val sizePictureInPixels: Int,
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
            itemView.findViewById<TextView>(R.id.itemName).text = item?.title ?: ""
            val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
            item?.urlToImage?.let { imageURL ->
                Glide.with(itemView)
                    .load(imageURL)
                    .override(sizePictureInPixels, sizePictureInPixels)
                    .into(imageView)
            }
        }
    }
}
