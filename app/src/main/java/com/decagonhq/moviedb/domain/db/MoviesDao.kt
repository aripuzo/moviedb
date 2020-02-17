package com.decagonhq.moviedb.domain.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.decagonhq.moviedb.domain.entities.Movie

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun observeMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun observeMovieById(movieId: Int): LiveData<Movie>

    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie): Int

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int): Int

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()
}