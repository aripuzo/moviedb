package com.decagonhq.moviedb.domain.source

import androidx.lifecycle.LiveData
import com.decagonhq.moviedb.domain.db.MoviesDao
import com.decagonhq.moviedb.domain.entities.Movie
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

    override suspend fun getAll(): List<Movie>? = withContext(ioDispatcher) {
        return@withContext try {
            moviesDao.getMovies()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun get(id: String): Movie? = withContext(ioDispatcher) {
        return@withContext try {
            null//moviesDao.getMovies()
        } catch (e: Exception) {
            null
        }
    }

    override fun getLocal(): LiveData<List<Movie>> {
        return moviesDao.observeMovies()
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
