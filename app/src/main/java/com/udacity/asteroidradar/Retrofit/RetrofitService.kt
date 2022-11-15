package com.udacity.asteroidradar.Retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDayNetwork
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


//   Moshi Builder to create a Moshi object with the KotlinJsonAdapterFactory
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .connectTimeout(100, TimeUnit.SECONDS)
    .readTimeout(100, TimeUnit.SECONDS)

    .addInterceptor { chain ->
        val url = chain
            .request()
            .url()
            .newBuilder()
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }
    .build()

private val retrofit = Retrofit.Builder()
    // ConverterFactory to the MoshiConverterFactory with our Moshi Object

    .addConverterFactory(ScalarsConverterFactory.create())//STRING
    .addConverterFactory(MoshiConverterFactory.create(moshi))

    .client(client)
    .baseUrl(BASE_URL)
    .build()

interface Json {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroid(
        @Query("api_key") apiKey: String = API_KEY,

        @Query("start_date") startDate: String = getNextSevenDaysFormattedDates().first(),
        @Query("end_date") endDate: String= getNextSevenDaysFormattedDates().last(),
    ): String // for API data.

    @GET("planetary/apod")
    suspend fun getImage(
        @Query("api_key") apiKey: String = API_KEY,
    ): PictureOfDayNetwork // for images.


    //https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY


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