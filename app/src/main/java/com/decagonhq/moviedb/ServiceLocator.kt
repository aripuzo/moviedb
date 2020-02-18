package com.decagonhq.moviedb

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.decagonhq.moviedb.domain.db.MovieDb
import com.decagonhq.moviedb.domain.entities.Movie
import com.decagonhq.moviedb.domain.repository.DefaultMoviesRepository
import com.decagonhq.moviedb.domain.repository.MoviesRepository
import com.decagonhq.moviedb.domain.source.MoviesDataSource
import com.decagonhq.moviedb.domain.source.MoviesLocalDataSource
import com.decagonhq.moviedb.domain.source.MoviesRemoteDataSource

/**
 * Created by ari on 2019-12-18.
 */
object ServiceLocator {

    private var database: MovieDb? = null

    @Volatile
    var moviesRepository: MoviesRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    fun provideTasksRepository(context: Context): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): MoviesRepository {
        val newRepo = DefaultMoviesRepository(MoviesRemoteDataSource(), createTaskLocalDataSource(context))
        moviesRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): MoviesDataSource<Movie> {
        val database = database ?: createDataBase(context)
        return MoviesLocalDataSource(database.moviesDao())
    }

    private fun createDataBase(context: Context): MovieDb {
        return MovieDb.getInstance(context)!!
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            moviesRepository = null
        }
    }
}