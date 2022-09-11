package com.example.moviedb.data.api

import com.example.moviedb.data.entities.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") key: String = "d3b018581c65b4ac18d55a61391e87ac"): Response<Result>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") key: String = "d3b018581c65b4ac18d55a61391e87ac"): Response<Result>

    @GET("movie/latest")
    suspend fun getLatestMovies(@Query("api_key") key: String = "d3b018581c65b4ac18d55a61391e87ac"): Response<Result>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("api_key") key: String = "d3b018581c65b4ac18d55a61391e87ac"): Response<Result>

}