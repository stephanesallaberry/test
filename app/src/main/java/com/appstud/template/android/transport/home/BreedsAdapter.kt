package com.appstud.template.android.transport.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.appstud.template.android.R
import com.appstud.template.android.domain.external.entity.Breed
import com.appstud.template.android.transport.utils.BaseAdapter
import com.bumptech.glide.Glide

class BreedsAdapter(
    private val sizePictureInPixels: Int,
    var list: List<Breed?>,
    onItemClicked: (Breed?) -> Unit
) :
    BaseAdapter<Breed?>(
        mData = list,
        itemLayoutId = R.layout.breed_item,
        onClickListener = onItemClicked
    ) {

    init {
        onBinding = { itemView: View, item: Breed?, _ ->
            itemView.findViewById<TextView>(R.id.breedItemName).text = item?.name ?: ""
            val imageView = itemView.findViewById<ImageView>(R.id.breedItemImage)
            item?.image?.url?.let { imageURL ->
                Glide.with(itemView)
                    .load(imageURL)
                    .override(sizePictureInPixels, sizePictureInPixels)
                    .into(imageView)
            }
        }
    }
}
