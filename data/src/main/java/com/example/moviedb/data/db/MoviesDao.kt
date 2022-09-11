package com.example.moviedb.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviedb.data.entities.Movie

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies WHERE category = :category")
    fun observeMovies(category: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM movies")
    fun observeMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun observeMovieById(movieId: Int): LiveData<Movie>

    @Query("SELECT * FROM movies WHERE category = :category")
    fun getMovies(category: Int): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Update
    fun updateMovie(movie: Movie): Int

    @Query("DELETE FROM movies WHERE id = :movieId")
    fun deleteMovieById(movieId: Int): Int

    @Query("DELETE FROM movies")
    fun deleteMovies()
}