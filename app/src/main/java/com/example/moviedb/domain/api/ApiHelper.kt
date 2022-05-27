package com.example.moviedb.domain.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getPopularMovies() = apiService.getPopularMovies()

    suspend fun getUpcomingMovies() = apiService.getUpcomingMovies()

    suspend fun getLatestMovies() = apiService.getLatestMovies()

    suspend fun searchMovies(query: String) = apiService.searchMovies(query)
}
