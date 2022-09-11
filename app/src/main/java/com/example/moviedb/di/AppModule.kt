package com.example.moviedb.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.content.res.Resources
import com.example.moviedb.data.db.MovieDb
import com.example.moviedb.data.db.MoviesDao
import com.example.moviedb.data.source.MoviesDataSource
import com.example.moviedb.data.source.MoviesLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideAppContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources

    @Singleton
    @Provides
    fun provideAssetManager(context: Context): AssetManager = context.assets

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}