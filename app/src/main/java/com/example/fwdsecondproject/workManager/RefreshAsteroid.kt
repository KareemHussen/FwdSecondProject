package com.example.fwdsecondproject.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fwdsecondproject.db.AsteroidDatabase
import com.example.fwdsecondproject.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroid(context: Context, params: WorkerParameters)
    : CoroutineWorker(context , params) {

    private val repository =
        AsteroidRepository(AsteroidDatabase.getDatabase(context).asteroidDao)

    override suspend fun doWork(): Result {
        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}