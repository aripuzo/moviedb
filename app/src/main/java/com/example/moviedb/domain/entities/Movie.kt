package com.example.moviedb.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by ari on 2020-02-16.
 */
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    var id: Int,
    var title: String? = null,
    var overview: String? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null,
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,
    @SerializedName("poster_path")
    var posterPath: String? = null,
//    @SerializedName("genre_ids")
//    val genreIDS: List<Long>? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    val adult: Boolean? = null,
    val popularity: Double? = null,
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Long? = null,
    @TypeConverters(MovieCategory.Converter::class)
    var category: MovieCategory? = null

) {
    fun getFullPosterImage(): String {
        return "https://image.tmdb.org/t/p/w500$posterPath"
    }
}

enum class MovieCategory(val value: Int) {
    POPULAR(0), LATEST(1), UPCOMING(2);

    object Converter {
        @TypeConverter
        fun toInt(movieCategory: MovieCategory): Int = movieCategory.ordinal

        @TypeConverter
        fun fromInt(int: Int): MovieCategory = values()[int]
    }
}