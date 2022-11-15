package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Repositry.Repo
import com.udacity.asteroidradar.room.AsteroidData
import retrofit2.HttpException

class Refresh (appContext: Context, params: WorkerParameters
      ): CoroutineWorker(appContext, params) {
        companion object {
            const val WORK_NAME = "RefreshDataWorker"
        }
        override suspend fun doWork(): Result {
            val database = AsteroidData.getDatabase(applicationContext)
            val repository = Repo(database)
            return try {

                repository.refresh()
                repository.delete()
                Result.success()

            } catch (e: HttpException) {
                Result.retry()
            }
        }
      }