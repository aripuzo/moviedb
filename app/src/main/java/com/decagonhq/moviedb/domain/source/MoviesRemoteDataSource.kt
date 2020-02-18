package com.decagonhq.moviedb.domain.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.decagonhq.moviedb.App
import com.decagonhq.moviedb.domain.entities.Movie
import com.decagonhq.moviedb.domain.entities.Result
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * Implementation of the data source that adds a latency simulating network.
 */
class MoviesRemoteDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesDataSource<Movie> {

    private val observableMovies = MutableLiveData<List<Movie>>()

    private val observableMovie = MutableLiveData<Movie>()

    override fun getLocal(): LiveData<List<Movie>> {
        return observableMovies
    }

    override fun observe(id: Int): LiveData<Movie> {
        return observableMovie
    }

    override suspend fun getAll() = suspendCoroutine<List<Movie>?> { cont ->
        val queue = Volley.newRequestQueue(App.applicationContext())
        val url = "https://api.themoviedb.org/4/discover/movie?sort_by=popularity.desc&api_key=d3b018581c65b4ac18d55a61391e87ac"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                val result = Gson().fromJson(response, Result::class.java)
                if(result != null)
                    cont.resume(result.results)
                else
                    cont.resume(null)
            },
            Response.ErrorListener { cont.resume(null) })

        queue.add(stringRequest)
    }

    override suspend fun get(id: String) = suspendCoroutine<Movie?> { cont ->
        val queue = Volley.newRequestQueue(App.applicationContext())
        val url = "https://api.themoviedb.org/3/movie/$id?api_key=d3b018581c65b4ac18d55a61391e87ac"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                cont.resume(Gson().fromJson(response, Movie::class.java))
            },
            Response.ErrorListener { cont.resume(null) })

        queue.add(stringRequest)
    }

    override suspend fun save(item: Movie) {

    }

    override suspend fun deleteAll() {

    }

    override suspend fun delete(item: Movie) {

    }
}
