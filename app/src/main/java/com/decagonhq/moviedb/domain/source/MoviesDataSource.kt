package com.decagonhq.moviedb.domain.source

import androidx.lifecycle.LiveData

/**
 * Main entry point for accessing tasks data.
 */
interface MoviesDataSource<T> {


    fun getLocal(): LiveData<List<T>>

    suspend fun getAll(): List<T>?

    suspend fun get(id: String): T?

    fun observe(id: Int): LiveData<T>

    suspend fun save(item: T)

    suspend fun deleteAll()

    suspend fun delete(item: T)
}
