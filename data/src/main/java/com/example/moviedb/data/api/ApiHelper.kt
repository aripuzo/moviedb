package com.example.moviedb.data.api

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {

    suspend fun getPopularMovies() = apiService.getPopularMovies()

    suspend fun getUpcomingMovies() = apiService.getUpcomingMovies()

    suspend fun getLatestMovies() = apiService.getLatestMovies()

    suspend fun searchMovies(query: String) = apiService.searchMovies(query)
}
