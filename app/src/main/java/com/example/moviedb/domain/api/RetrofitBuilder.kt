package com.example.moviedb.domain.api

import com.example.moviedb.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val timeOutSec = 30L

    private val loggingInterceptor = HttpLoggingInterceptor {
        Timber.i(it);
    }

    private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            .build() //Doesn't require the adapter
    }

    private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(timeOutSec, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(timeOutSec, TimeUnit.SECONDS)
            .build()
    } else OkHttpClient
        .Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .connectTimeout(timeOutSec, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(timeOutSec, TimeUnit.SECONDS)
        .build()


    val apiService: ApiService = getRetrofit(provideOkHttpClient()).create(ApiService::class.java)
}