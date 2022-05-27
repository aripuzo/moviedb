package com.example.moviedb.viewmodel

import androidx.lifecycle.*
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory
import com.example.moviedb.domain.repository.MoviesRepository
import com.example.moviedb.utils.Resource
import kotlinx.coroutines.launch

/**
 * ViewModel for the task list screen.
 */
class MovieViewModel(
    private val moviesRepository: MoviesRepository,
    private val category: MovieCategory
    ) : ViewModel() {


    internal val _remoteResponse = MutableLiveData<Resource<com.example.moviedb.domain.entities.Result?>>()
    val remoteResponse: LiveData<Resource<com.example.moviedb.domain.entities.Result?>> = _remoteResponse

    var moviesResponse: LiveData<List<Movie>>? = null

    init {
        viewModelScope.launch {
            moviesResponse = moviesRepository.getLocalMovies(category)
        }
    }

    fun getMovies() = viewModelScope.launch {
        _remoteResponse.postValue(Resource.loading(data = null))
        try {
            moviesRepository.getRemoteMovies(category).let {
                if (it.isSuccessful) {
                    val response = it.body()
                    _remoteResponse.postValue(Resource.success(response))
                    if (response?.results != null) {
                        moviesRepository.saveMovies(response.results, category)
                    }
                } else {
                    _remoteResponse.postValue(Resource.error(data = null, message = "Request failed!"))
                }
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            _remoteResponse.postValue(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun searchMovies(query: String) = viewModelScope.launch {
        println("........................................................0")
        _remoteResponse.postValue(Resource.loading(data = null))
        try {
            moviesRepository.searchMovies(query).let {
                if (it.isSuccessful) {
                    val response = it.body()
                    println("........................................................1")
                    _remoteResponse.postValue(Resource.success(response))
                } else {
                    _remoteResponse.postValue(Resource.error(data = null, message = "Search failed!"))
                }
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            _remoteResponse.postValue(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}
