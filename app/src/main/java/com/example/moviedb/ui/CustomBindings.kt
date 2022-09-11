package com.example.moviedb.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.moviedb.data.entities.Movie


/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Movie>?) {
    items?.let {
        (listView.adapter as MoviesAdapter).submitList(items)
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(
                RequestOptions()//.placeholder(R.drawable.avatar).error(R.drawable.avatar)
                    .centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}