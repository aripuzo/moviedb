package com.example.moviedb.viewmodel

import androidx.lifecycle.*
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.entities.MovieCategory
import com.example.moviedb.data.entities.Result
import com.example.moviedb.data.repository.MoviesRepository
import com.example.moviedb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
    ) : ViewModel() {


    internal val _remoteResponse = MutableLiveData<Resource<Result?>>()
    val remoteResponse: LiveData<Resource<Result?>> = _remoteResponse

    var moviesResponse: LiveData<List<Movie>>? = null

    init {
        viewModelScope.launch {
            moviesResponse = moviesRepository.getLocalMovies()
        }
    }

    fun getMovies(category: MovieCategory) = viewModelScope.launch {
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
        _remoteResponse.postValue(Resource.loading(data = null))
        try {
            moviesRepository.searchMovies(query).let {
                if (it.isSuccessful) {
                    val response = it.body()
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
