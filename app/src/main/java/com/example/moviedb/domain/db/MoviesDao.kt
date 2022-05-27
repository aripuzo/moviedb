package com.example.moviedb.domain.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies WHERE category = :category")
    fun observeMovies(category: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM movies")
    fun observeMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun observeMovieById(movieId: Int): LiveData<Movie>

    @Query("SELECT * FROM movies WHERE category = :category")
    suspend fun getMovies(category: Int): List<Movie>

    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Update
    suspend fun updateMovie(movie: Movie): Int

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int): Int

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()
}