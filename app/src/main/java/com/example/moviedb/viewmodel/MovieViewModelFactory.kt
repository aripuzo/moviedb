package com.example.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.domain.entities.MovieCategory
import com.example.moviedb.domain.repository.MoviesRepository

/**
 * Created by ari on 2019-12-18.
 */
@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory (
        private val tasksRepository: MoviesRepository,
        private val category: MovieCategory
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (MovieViewModel(tasksRepository, category) as T)
}