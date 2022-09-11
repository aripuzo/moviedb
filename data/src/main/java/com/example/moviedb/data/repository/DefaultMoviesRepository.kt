package com.example.moviedb.data.repository

import com.example.moviedb.data.api.ApiHelper
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.entities.MovieCategory
import com.example.moviedb.data.entities.Result
import com.example.moviedb.data.source.MoviesDataSource
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class DefaultMoviesRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val localDataSource: MoviesDataSource,
    private val ioDispatcher: CoroutineDispatcher) : MoviesRepository {

    override suspend fun searchMovies(query: String): Response<Result> {
        return apiHelper.searchMovies(query)
    }

    override suspend fun getRemoteMovies(category: MovieCategory): Response<Result> {
        return when(category) {
            MovieCategory.LATEST -> apiHelper.getLatestMovies()
            MovieCategory.POPULAR -> apiHelper.getPopularMovies()
            MovieCategory.UPCOMING -> apiHelper.getUpcomingMovies()
            else -> apiHelper.getLatestMovies()
        }
    }

    override suspend fun getLocalMovies() = localDataSource.observeMovies()


    override suspend fun getMovie(id: Int, forceUpdate: Boolean): Movie? {
        if (forceUpdate) {
            //updateTaskFromRemoteDataSource(id)
        }
        return localDataSource.get(id)
    }

    override suspend fun saveMovie(movie: Movie) {
        coroutineScope {
            launch { localDataSource.save(movie) }
        }
    }

    override suspend fun saveMovies(movies: List<Movie>, category: MovieCategory) {
        coroutineScope {
            launch { localDataSource.save(movies, category) }
        }
    }

    override suspend fun deleteAllMovies() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { localDataSource.deleteAll() }
            }
        }
    }

    override suspend fun deleteMovie(movie: Movie) {
        coroutineScope {
            launch { localDataSource.delete(movie) }
        }
    }
}
