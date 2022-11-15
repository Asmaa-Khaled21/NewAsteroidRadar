package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.room.AsteroidEntity
import com.udacity.asteroidradar.room.ImageEntity

data class PictureOfDay(  @Json(name = "media_type" )
                          val media_Type: String,
                          val title: String,
                          val url: String)


@JsonClass(generateAdapter = true)
data class PictureOfDayNetwork(
    val url : String,
    val date : String,
    val media_Type : String,
    val title : String)

fun PictureOfDayNetwork.asImageDatabase(): ImageEntity {
    return ImageEntity(
        url, date, media_Type, title
    )
}
