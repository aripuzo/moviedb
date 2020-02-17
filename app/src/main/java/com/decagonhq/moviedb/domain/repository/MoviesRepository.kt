package com.decagonhq.moviedb.domain.repository

import androidx.lifecycle.LiveData
import com.decagonhq.moviedb.domain.entities.Movie
import com.decagonhq.moviedb.domain.entities.Result

interface MoviesRepository {
    suspend fun getMovies(forceUpdate: Boolean = false): Result<List<Movie>>

    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Result<Movie>

    suspend fun saveMovie(movie: Movie)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllMovies()

    suspend fun deleteMovie(movie: Movie)

    fun getFavouriteMovies(): LiveData<Result<List<Movie>>>
}