package com.decagonhq.moviedb.viewmodel

import androidx.lifecycle.*
import com.decagonhq.moviedb.domain.entities.Movie
import com.decagonhq.moviedb.domain.repository.MoviesRepository

/**
 * ViewModel for the task list screen.
 */
class MovieViewModel( private val moviesRepository: MoviesRepository) : ViewModel() {

    val items: MutableLiveData<List<Movie>> = MutableLiveData()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _tasksAddViewVisible = MutableLiveData<Boolean>()
    val tasksAddViewVisible: LiveData<Boolean> = _tasksAddViewVisible

    private var resultMessageShown: Boolean = false

    suspend fun favoriteMovie(movie: Movie) {
        moviesRepository.saveMovie(movie)
    }

    suspend fun unfavouriteMovie(movie: Movie) {
        moviesRepository.deleteMovie(movie)
    }
    suspend fun getPopularMovies() {
        items.postValue(moviesRepository.getMovies(true))
    }

    fun getFavoriteMovies() = moviesRepository.getFavouriteMovies()
}
