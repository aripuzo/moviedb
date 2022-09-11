package com.example.moviedb.di

import com.example.moviedb.data.repository.DefaultMoviesRepository
import com.example.moviedb.data.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideUserRepository(moviesRepository: DefaultMoviesRepository): MoviesRepository =
        moviesRepository
}