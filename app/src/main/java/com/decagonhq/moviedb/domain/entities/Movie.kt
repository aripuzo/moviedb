package com.decagonhq.moviedb.domain.entities

import androidx.room.Entity

/**
 * Created by ari on 2020-02-16.
 */
@Entity(tableName = "movies")
data class Movie(
    var id: Int
)