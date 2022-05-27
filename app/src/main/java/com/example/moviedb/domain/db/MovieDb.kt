package com.example.moviedb.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory

@Database(entities = [(Movie::class)],
        version = 1,
        exportSchema = false)
@TypeConverters(DateConverter::class, MovieCategory.Converter::class)
abstract class MovieDb : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDb? = null

        fun getInstance(context: Context): MovieDb? {
            if (INSTANCE == null) {
                synchronized(MovieDb::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MovieDb::class.java, "movie_db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

