package com.udacity.asteroidradar.Repositry


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.Retrofit.AsteroidApi
import com.udacity.asteroidradar.api.Utils
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.room.AsteroidData
import com.udacity.asteroidradar.room.asAsteroidEntity
import com.udacity.asteroidradar.room.asImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.util.*


class Repo(val database: AsteroidData) {

    val timeToday = Utils.formattedString(Calendar.getInstance().time, Constants.API_QUERY_DATE_FORMAT)

    val timeWeek = Utils.formattedString(
        Utils.addDaysToDate(Calendar.getInstance().time, 7),
        Constants.API_QUERY_DATE_FORMAT
    )

    val asteroidAll: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDAO.getAsteroid()) {
            it.asAsteroidEntity()
        }
    val asteroidToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDAO.getTodayAsteroid(timeToday)) {
            it.asAsteroidEntity()
        }
    val asteroidWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDAO.getWeekAsteroid(timeToday, timeWeek)) {
            it.asAsteroidEntity()
        }


    ///////////////////  Image ////////////////

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.imageDao.getImage()) {
            it?.asImageEntity()
        }

    /////////////////// Refresh /////////////////////
    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidJson = AsteroidApi.jsonAPI.getAsteroid()
                val imageResponse = AsteroidApi.jsonAPI.getImage()
                val jsonObject = JSONObject(asteroidJson)
                Log.d("jsonObject",jsonObject.toString())
                Log.d("jsonObject",asteroidJson.toString())
                Log.d("jsonObject",imageResponse.toString())

                val asteroidsArrayList = parseAsteroidsJsonResult(jsonObject)
                Log.d("jsonObjectasteroidsArrayList",asteroidsArrayList.toString())

                database.asteroidDAO.insertAllAsteroid(*asteroidsArrayList.asAsteroidDatabase())
                database.imageDao.insertImage(imageResponse.asImageDatabase())
            } catch (e: Exception) {
                Timber.e(e)

                Log.d("jsonObject",e.toString())
            }
        }
    }

    /////////////////// delete /////////////////////
    suspend fun delete() {
        withContext(Dispatchers.IO){
            database.asteroidDAO.delete(timeToday)
        }
    }
}