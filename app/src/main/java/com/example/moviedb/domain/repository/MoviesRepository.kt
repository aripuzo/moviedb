package com.example.moviedb.domain.repository

import androidx.lifecycle.LiveData
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory
import com.example.moviedb.domain.entities.Result
import retrofit2.Response

interface MoviesRepository {
    suspend fun searchMovies(query: String): Response<Result>

    suspend fun getRemoteMovies(category: MovieCategory): Response<Result>

    suspend fun getLocalMovies(category: MovieCategory): LiveData<List<Movie>>?

    suspend fun getMovie(id: Int, forceUpdate: Boolean = false): Movie?

    suspend fun saveMovie(movie: Movie)

    suspend fun saveMovies(movies: List<Movie>, category: MovieCategory)

    suspend fun deleteAllMovies()

    suspend fun deleteMovie(movie: Movie)
}