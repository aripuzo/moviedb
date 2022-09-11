package com.example.moviedb.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.ListItemMovieBinding
import com.example.moviedb.data.entities.Movie

/**
 * Adapter for the [RecyclerView] in [MoviesFragment].
 */
class MoviesAdapter: ListAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesDiffCallback()) {

    private var clickListener: Callback<Movie>? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mda = getItem(position)
        holder.apply {
            bind(createClickListener(mda), mda)
        }
    }

    private fun createClickListener(movie: Movie): View.OnClickListener {
        return View.OnClickListener {
            clickListener?.invoke(movie)
        }
    }

    fun setClickListener(clickListener: Callback<Movie>) {
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

        fun bind(likeListener: View.OnClickListener,
                 item: Movie
        ) {
            binding.apply {
                movie = item
                clickListener = likeListener
                executePendingBindings()
            }
        }
    }
}

typealias Callback<T> = (T) -> Unit