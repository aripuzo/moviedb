package com.decagonhq.moviedb.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decagonhq.moviedb.domain.entities.Movie

/**
 * Adapter for the [RecyclerView] in [MoviesFragment].
 */
class MoviesAdapter: ListAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesDiffCallback()) {

    private var clickListener: Callback<Int>? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mda = getItem(position)
        holder.apply {
            bind(createLikeClickListener(position), createReplyClickListener(position), mda,
                mda.userId == user!!.id || mda.userId == "userId")
            itemView.tag = mda
        }
    }

    private fun createClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            clickListener?.invoke(position)
        }
    }

    fun setClickListener(clickListener: Callback<Int>) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(
        private val binding: ListItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(likeListener: View.OnClickListener, replyListener: View.OnClickListener,
                 item: Movie) {
            binding.apply {
                comment = item
                clickListener = likeListener
                executePendingBindings()
            }
        }
    }
}