package com.example.moviedb

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.moviedb.domain.api.ApiHelper
import com.example.moviedb.domain.api.RetrofitBuilder
import com.example.moviedb.domain.db.MovieDb
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.repository.DefaultMoviesRepository
import com.example.moviedb.domain.repository.MoviesRepository
import com.example.moviedb.domain.source.MoviesDataSource
import com.example.moviedb.domain.source.MoviesLocalDataSource

/**
 * Created by ari on 2019-12-18.
 */
object ServiceLocator {

    private var database: MovieDb? = null

    @Volatile
    var moviesRepository: MoviesRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    fun provideMovieRepository(context: Context): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMovieRepository(context)
        }
    }

    private fun createMovieRepository(context: Context): MoviesRepository {
        val newRepo = DefaultMoviesRepository(ApiHelper(RetrofitBuilder.apiService), createMovieLocalDataSource(context))
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMovieLocalDataSource(context: Context): MoviesDataSource<Movie> {
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