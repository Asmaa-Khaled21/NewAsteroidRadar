package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidReaderApp: Application() {
val applicationScope= CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit()=applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder() // constraints to fetch data
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()
        val repeatingRequest = PeriodicWorkRequestBuilder<Refresh>(1, TimeUnit.DAYS)// repeat fetch data
            .setConstraints(constraints).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(//repeat fetch data
            Refresh.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
}