package com.decagonhq.moviedb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.decagonhq.moviedb.domain.repository.MoviesRepository

/**
 * Created by ari on 2019-12-18.
 */
@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory (
        private val tasksRepository: MoviesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (MovieViewModel(tasksRepository) as T)
}