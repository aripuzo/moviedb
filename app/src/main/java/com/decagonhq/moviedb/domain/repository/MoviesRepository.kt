package com.decagonhq.moviedb.domain.repository

import androidx.lifecycle.LiveData
import com.decagonhq.moviedb.domain.entities.Movie

interface MoviesRepository {
    suspend fun getMovies(forceUpdate: Boolean = false): List<Movie>?

    suspend fun getMovie(id: String, forceUpdate: Boolean = false): Movie?

    suspend fun saveMovie(movie: Movie)

    suspend fun deleteAllMovies()

    suspend fun deleteMovie(movie: Movie)

    fun getFavouriteMovies(): LiveData<List<Movie>>
}