package com.example.moviedb.data.di

import android.content.Context
import com.example.moviedb.data.api.ApiHelper
import com.example.moviedb.data.api.ApiService
import com.example.moviedb.data.db.MovieDb
import com.example.moviedb.data.db.MoviesDao
import com.example.moviedb.data.source.MoviesDataSource
import com.example.moviedb.data.source.MoviesLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    private val TIMEOUT = 10L

    private val BASE_URL = "https://api.themoviedb.org/3/"

    @Singleton
    @Provides
    fun provideOkHttpCache(context: Context): Cache =
        Cache(context.cacheDir, (10 * 1024 * 1024).toLong())

    @Singleton
    @Provides
    @Named("logging")
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor{
            Timber.i(it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @Named("logging") logging: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .apply {
                addInterceptor(logging)
//                if (BuildConfig.DEBUG) addInterceptor(OkHttpProfilerInterceptor())
                //addInterceptor(header)
//                authenticator(authenticator)
                //if (BuildConfig.DEBUG && BuildConfig.MOCK_DATA) addInterceptor(mockInterceptor)
            }
            .build()

    @Singleton
    @Provides
    fun provideAppRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideApiHelper(apiService: ApiService): ApiHelper = ApiHelper(apiService)

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): MovieDb =
        MovieDb.getInstance(context)

    @Singleton
    @Provides
    fun provideMovieDao(movieDb: MovieDb): MoviesDao = movieDb.moviesDao()

    @Singleton
    @Provides
    fun provideUserRepository(moviesRepository: MoviesLocalDataSource): MoviesDataSource =
        moviesRepository
}