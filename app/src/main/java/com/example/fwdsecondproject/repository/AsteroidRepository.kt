package com.example.fwdsecondproject.repository


import android.annotation.SuppressLint
import com.example.fwdsecondproject.Constants
import com.example.fwdsecondproject.api.NasaClient
import com.example.fwdsecondproject.api.parseAsteroidsJsonResult
import com.example.fwdsecondproject.db.AsteroidDao
import com.example.fwdsecondproject.models.Asteroid
import com.example.fwdsecondproject.models.PictureOfDay
import kotlinx.coroutines.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val dbDao: AsteroidDao) {

    fun getToday(): String {
        val calendar = Calendar.getInstance()
        return formatDate(calendar.time)
    }

    private fun getNextWeak(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        return formatDate(calendar.time)
    }

    suspend fun refreshAsteroids(): ArrayList<Asteroid>{

        var asteroids: ArrayList<Asteroid>
        withContext(Dispatchers.IO){
            val response = NasaClient.api.getAsteroids(getToday()).await()
            asteroids = parseAsteroidsJsonResult(JSONObject(response.string()))

            for(astr in asteroids){
                dbDao.insertAsteroid(astr)
            }
        }
        return asteroids
    }

    fun getTodayForNextWeekAsteroids() : List<Asteroid>{
        return dbDao.getAsteroidsByCloseApproachDate(getToday(),getNextWeak())
    }

    fun deletePreviousAsteroids(){
        dbDao.deletePreviousDayAsteroids(getToday())
    }

    suspend fun getPictureOfDay() : PictureOfDay{
        return NasaClient.api.getPictureOfDay().await()

    }

    fun getAllAsteroids (): List<Asteroid> {
        return dbDao.getAllAsteroids()
    }



    @SuppressLint("WeekBasedYear")
    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }

}