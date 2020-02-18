package com.decagonhq.moviedb.domain.entities

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by ari on 2020-02-16.
 */
@Entity(tableName = "movies")
data class Movie(
    var id: Int,
    var title: String? = null,
    var overview: String? = null,
    @SerializedName("release_date")
    var releaseDate: Date? = null,
    @SerializedName("backdrop_path")
    var backdropPath: Date? = null,
    @SerializedName("poster_path")
    var posterPath: Date? = null

)