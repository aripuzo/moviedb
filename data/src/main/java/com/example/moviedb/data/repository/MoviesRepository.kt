package com.example.moviedb.data.repository

import androidx.lifecycle.LiveData
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.entities.MovieCategory
import com.example.moviedb.data.entities.Result
import retrofit2.Response

interface MoviesRepository {
    suspend fun searchMovies(query: String): Response<Result>

    suspend fun getRemoteMovies(category: MovieCategory): Response<Result>

    suspend fun getLocalMovies(): LiveData<List<Movie>>?

    suspend fun getMovie(id: Int, forceUpdate: Boolean = false): Movie?

    suspend fun saveMovie(movie: Movie)

    suspend fun saveMovies(movies: List<Movie>, category: MovieCategory)

    suspend fun deleteAllMovies()

    suspend fun deleteMovie(movie: Movie)
}