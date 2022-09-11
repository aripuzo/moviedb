package com.example.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    internal val _remoteResponse = MutableLiveData<String?>()
    val remoteResponse: LiveData<String?> = _remoteResponse
    val movie = MutableLiveData<Movie>()

    fun checkFavorite(id: Int) {
        viewModelScope.launch {
            try {
                val favoriteMovie = moviesRepository.getMovie(id, false)
                withContext(Dispatchers.Main) {
                    if(favoriteMovie != null) {
                        val newMoview = movie.value
                        newMoview?.isFavorite = true
                        movie.postValue(newMoview!!)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun favoriteOrUnFavoriteMovie(){
        val newMovie = movie.value
        if(newMovie?.isFavorite == true)
            unFavoriteMovie()
        else
            favoriteMovie()
    }

    private fun favoriteMovie() {
        val newMovie = movie.value
        newMovie?.isFavorite = true
        movie.postValue(newMovie!!)

        newMovie.let {
            viewModelScope.launch {
                try {
                    moviesRepository.saveMovie(it)
                    _remoteResponse.postValue("Movie added to favourite list")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun unFavoriteMovie() {
        val newMovie = movie.value
        newMovie?.isFavorite = false
        movie.value = newMovie!!

        newMovie.let {
            viewModelScope.launch {
                try {
                    moviesRepository.deleteMovie(it)
                    _remoteResponse.postValue("Movie removed from favorite list")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
