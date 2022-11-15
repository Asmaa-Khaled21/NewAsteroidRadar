package com.udacity.asteroidradar.Retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDayNetwork
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


//   Moshi Builder to create a Moshi object with the KotlinJsonAdapterFactory
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//fun client(): OkHttpClient {
//    val logging = HttpLoggingInterceptor()
//// set your desired log level
//// set your desired log level
//    logging.level = HttpLoggingInterceptor.Level.BODY
//    val httpClient = OkHttpClient.Builder()
//// add your other interceptors …
//// add logging as last interceptor
//// add your other interceptors …
//// add logging as last interceptor
//    httpClient.addInterceptor(logging)
//    return httpClient.build()
//}

private val retrofit = Retrofit.Builder()
    // ConverterFactory to the MoshiConverterFactory with our Moshi Object
    .addConverterFactory(ScalarsConverterFactory.create())//STRING
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface Json {
    @GET("neo/rest/v1/feed?api_key=" + Constants.API_KEY)
    suspend fun getAsteroid(): String // for API data.

    @GET("planetary/apod?api_key=" + Constants.API_KEY)
    suspend fun getImage(): PictureOfDayNetwork // for images.
}

object AsteroidApi {

    val jsonAPI: Json by lazy {
        retrofit.create(Json::class.java)
    }

    @Deprecated("", ReplaceWith("AsteroidApi.jsonAPI"))
    val asteroidJson: Json by lazy {
        retrofit.create(Json::class.java)
    }

    @Deprecated("", ReplaceWith("AsteroidApi.jsonAPI"))
    val imageResponse: Json by lazy {
        retrofit.create(Json::class.java)
    }
}