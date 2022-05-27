package com.example.moviedb.domain.repository

import androidx.lifecycle.Observer
import com.example.moviedb.domain.api.ApiHelper
import com.example.moviedb.domain.entities.Movie
import com.example.moviedb.domain.entities.MovieCategory
import com.example.moviedb.domain.entities.Result
import com.example.moviedb.domain.source.MoviesDataSource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
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

    @Mock
    lateinit var apiHelper: ApiHelper

    @Mock
    lateinit var dataSource: MoviesDataSource<Movie>

    @Mock
    lateinit var fakeMovies: List<Movie>

    @Before
    fun setUp() {
        dataSource = mock()
        apiHelper = mock()
        apiHelper.stub {
            onBlocking { getPopularMovies() }.doReturn(Response.success(Result(1, fakeMovies)))
        }
//        dataSource.stub {
//            onBlocking { observeMovies(CATEGORY) }.doReturn(liveDa)
//        }
        defaultMoviesRepository = DefaultMoviesRepository(apiHelper, dataSource)
    }

    @Test
    fun `get remote movies will return list of movies` () {
        runBlocking {
            val response = defaultMoviesRepository?.getRemoteMovies(CATEGORY)
            assert(response?.isSuccessful == true)
            assert(response?.body()?.results?.isNotEmpty() == true)
        }
    }

//    @Test
//    fun `get local movies will return list of movies` () {
//
//        val expectedResponse: Single<ExchangeRateResponse> =
//            org.graalvm.compiler.nodes.memory.MemoryCheckpoint.Single.just(ExchangeRateResponse.EMPTY)
//        runBlocking {
//            val response = defaultMoviesRepository?.getLocalMovies(CATEGORY)
//            assert(response?.value == true)
//            assert(response?.value?.isNotEmpty() == true)
//        }
//    }

}