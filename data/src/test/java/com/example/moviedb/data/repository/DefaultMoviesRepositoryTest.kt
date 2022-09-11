package com.example.moviedb.data.repository

import com.example.moviedb.data.api.ApiHelper
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.entities.MovieCategory
import com.example.moviedb.data.entities.Result
import com.example.moviedb.data.source.MoviesDataSource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class DefaultMoviesRepositoryTest {

    private var defaultMoviesRepository: DefaultMoviesRepository? = null

    private val CATEGORY = MovieCategory.POPULAR

    private val query = "Fake query"

    @Mock
    lateinit var apiHelper: ApiHelper

    @Mock
    lateinit var dataSource: MoviesDataSource

    @Mock
    lateinit var ioDispatcher: CoroutineDispatcher

    @Mock
    lateinit var fakeMovies: List<Movie>

    @Before
    fun setUp() {
        dataSource = mock()
        apiHelper = mock()
        ioDispatcher = mock()
        apiHelper.stub {
            onBlocking { getPopularMovies() }.doReturn(Response.success(Result(1, fakeMovies)))
            onBlocking { searchMovies(query) }.doReturn(Response.success(Result(1, fakeMovies)))
        }
        defaultMoviesRepository = DefaultMoviesRepository(apiHelper, dataSource, ioDispatcher)
    }

    @Test
    fun `get remote movies will return list of movies` () {
        runBlocking {
            val response = defaultMoviesRepository?.getRemoteMovies(CATEGORY)
            assert(response?.isSuccessful == true)
            assert(response?.body()?.results?.isNotEmpty() == true)
        }
    }

    @Test
    fun `search movies will return list of movies` () {
        runBlocking {
            val response = defaultMoviesRepository?.searchMovies(query)
            assert(response?.isSuccessful == true)
            assert(response?.body()?.results?.isNotEmpty() == true)
        }
    }

}