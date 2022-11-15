package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.room.ImageEntity

data class PictureOfDay(@Json(name = "media_type" )
                        val media_type: String,
                        val title: String,
                        val url: String)


@JsonClass(generateAdapter = true)
data class PictureOfDayNetwork(
    val media_type: String,
    val title: String,
    val url: String)

fun PictureOfDayNetwork.asImageDatabase(): ImageEntity {
    return ImageEntity(
        title =title,
        url=url,

        media_type=media_type,

        )
}
