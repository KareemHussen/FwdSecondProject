package com.example.fwdsecondproject.workManager


import android.app.Application
import android.util.Log
import androidx.work.*
import com.example.fwdsecondproject.Constants
import kotlinx.coroutines.*

import java.util.concurrent.TimeUnit

class WorkManagerScheduler : Application() {

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            setupWorker()
        }
    }

    private fun setupWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                setRequiresDeviceIdle(true)
            }.build()
        Log.d("meow" , "In setup method")

        val refreshRequest = PeriodicWorkRequestBuilder<RefreshAsteroid>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Constants.REFRESH_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )

        val deleteRequest = PeriodicWorkRequestBuilder<DeleteAsteroid>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Constants.DELETE_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            deleteRequest
        )

    }
}