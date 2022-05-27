package com.example.moviedb.domain.source

import androidx.lifecycle.LiveData
import com.example.moviedb.domain.db.MoviesDao
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class MoviesLocalDataSource internal constructor(
    private val moviesDao: MoviesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesDataSource<Movie> {

    override suspend fun getMovies(category: MovieCategory): List<Movie>? = withContext(ioDispatcher) {
        return@withContext try {
            moviesDao.getMovies(category.ordinal)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun observeMovies(category: MovieCategory): LiveData<List<Movie>>?  = withContext(ioDispatcher) {
        return@withContext try {
            moviesDao.observeMovies()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun save(items: List<Movie>, category: MovieCategory) {
        items.forEach {
            it.category = category
        }
        moviesDao.insertMovies(items)
    }

    override suspend fun get(id: Int): Movie? = withContext(ioDispatcher) {
        return@withContext try {
            moviesDao.getMovieById(id)
        } catch (e: Exception) {
            null
        }
    }

    override fun observe(id: Int): LiveData<Movie> {
       return moviesDao.observeMovieById(id)
    }

    override suspend fun save(item: Movie) {
        moviesDao.insertMovie(item)
    }

    override suspend fun deleteAll() {
        moviesDao.deleteMovies()
    }

    override suspend fun delete(item: Movie) {
        moviesDao.deleteMovieById(item.id)
    }
}
