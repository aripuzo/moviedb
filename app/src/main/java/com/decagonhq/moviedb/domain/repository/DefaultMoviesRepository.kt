package com.decagonhq.moviedb.domain.repository

import androidx.lifecycle.LiveData
import com.decagonhq.moviedb.domain.entities.Movie
import com.decagonhq.moviedb.domain.source.MoviesDataSource
import kotlinx.coroutines.*

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class DefaultMoviesRepository(
    private val tasksRemoteDataSource: MoviesDataSource<Movie>,
    private val tasksLocalDataSource: MoviesDataSource<Movie>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : MoviesRepository {

    override fun getFavouriteMovies(): LiveData<List<Movie>> {
        return tasksLocalDataSource.getLocal()
    }

//    private suspend fun updateTaskFromRemoteDataSource(taskId: String) {
//        val remoteTask = tasksRemoteDataSource.getTask(taskId)
//
//        if (remoteTask is Success) {
//            tasksLocalDataSource.saveTask(remoteTask.data)
//        }
//    }

    override suspend fun getMovies(forceUpdate: Boolean): List<Movie>? {
        return tasksRemoteDataSource.getAll()
    }

    override suspend fun getMovie(id: String, forceUpdate: Boolean): Movie? {
        if (forceUpdate) {
            //updateTaskFromRemoteDataSource(id)
        }
        return tasksLocalDataSource.get(id)
    }

    override suspend fun saveMovie(movie: Movie) {
        coroutineScope {
            launch { tasksLocalDataSource.save(movie) }
        }
    }

    override suspend fun deleteAllMovies() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksLocalDataSource.deleteAll() }
            }
        }
    }

    override suspend fun deleteMovie(movie: Movie) {
        coroutineScope {
            launch { tasksLocalDataSource.delete(movie) }
        }
    }
}
