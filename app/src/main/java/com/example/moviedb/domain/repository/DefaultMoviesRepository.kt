package com.example.moviedb.domain.repository

import com.example.moviedb.domain.api.ApiHelper
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory
import com.example.moviedb.domain.entities.Result
import com.example.moviedb.domain.source.MoviesDataSource
import kotlinx.coroutines.*
import retrofit2.Response

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class DefaultMoviesRepository(
    private val apiHelper: ApiHelper,
    private val localDataSource: MoviesDataSource<Movie>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : MoviesRepository {

    override suspend fun searchMovies(query: String): Response<Result> {
        return apiHelper.searchMovies(query)
    }

    override suspend fun getRemoteMovies(category: MovieCategory): Response<Result> {
        return when(category) {
            MovieCategory.LATEST -> apiHelper.getLatestMovies()
            MovieCategory.POPULAR -> apiHelper.getPopularMovies()
            MovieCategory.UPCOMING -> apiHelper.getUpcomingMovies()
        }
    }

    override suspend fun getLocalMovies(category: MovieCategory) = localDataSource.observeMovies(category)


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
