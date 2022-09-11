package com.example.moviedb.data.source

import androidx.lifecycle.LiveData
import com.example.moviedb.data.db.MoviesDao
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.entities.MovieCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Concrete implementation of a data source as a db.
 */
class MoviesLocalDataSource @Inject internal constructor(
    private val moviesDao: MoviesDao,
    private val ioDispatcher: CoroutineDispatcher
) : MoviesDataSource {

    override suspend fun getMovies(category: MovieCategory): List<Movie>? = withContext(ioDispatcher) {
        return@withContext try {
            moviesDao.getMovies(category.ordinal)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun observeMovies(): LiveData<List<Movie>>?  = withContext(ioDispatcher) {
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
