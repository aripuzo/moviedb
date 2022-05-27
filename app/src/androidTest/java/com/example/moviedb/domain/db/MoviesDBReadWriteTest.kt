package com.example.moviedb.domain.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MoviesDBReadWriteTest {
    private lateinit var moviesDao: MoviesDao
    private lateinit var db: MovieDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MovieDb::class.java).build()
        moviesDao = db.moviesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        runBlocking {
            val movie: Movie = Gson().fromJson(
                "{\"adult\":false,\"backdrop_path\":\"/1Rr5SrvHxMXHu5RjKpaMba8VTzi.jpg\",\"genre_ids\":[28,12,878],\"id\":634649,\"original_language\":\"en\",\"original_title\":\"Spider-Man: No Way Home\",\"overview\":\"Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.\",\"popularity\":7553.322,\"poster_path\":\"/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg\",\"release_date\":\"2021-12-15\",\"title\":\"Spider-Man: No Way Home\",\"video\":false,\"vote_average\":8.4,\"vote_count\":3644}",
                Movie::class.java
            )
            movie.category = MovieCategory.POPULAR

            val byName = moviesDao.getMovies(MovieCategory.POPULAR.value)
            assertThat(byName[0], equalTo(movie))
        }
    }
}