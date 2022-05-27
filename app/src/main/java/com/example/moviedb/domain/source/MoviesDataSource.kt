package com.example.moviedb.domain.source

import androidx.lifecycle.LiveData
import com.example.moviedb.domain.entities.MovieCategory

/**
 * Main entry point for accessing tasks data.
 */
interface MoviesDataSource<Movie> {

    suspend fun getMovies(category: MovieCategory): List<Movie>?

    suspend fun observeMovies(category: MovieCategory): LiveData<List<Movie>>?

    suspend fun get(id: Int): Movie?

    fun observe(id: Int): LiveData<Movie>

    suspend fun save(item: Movie)

    suspend fun save(items: List<Movie>, category: MovieCategory)

    suspend fun deleteAll()

    suspend fun delete(item: Movie)
}
