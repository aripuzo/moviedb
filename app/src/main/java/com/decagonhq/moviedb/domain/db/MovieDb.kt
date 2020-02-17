package com.decagonhq.moviedb.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.decagonhq.moviedb.domain.entities.Movie

@Database(entities = [(Movie::class)],
        version = 1,
        exportSchema = false)
//@TypeConverters(DateConverter::class)
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

