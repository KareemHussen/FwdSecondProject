package com.example.fwdsecondproject.db

import androidx.room.*
import com.example.fwdsecondproject.models.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM Asteroid " +
            "WHERE closeApproachDate >= :startDate " +
            "AND closeApproachDate <= :endDate " +
            "ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): List<Asteroid>

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): List<Asteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(vararg asteroid: Asteroid)

    @Query("DELETE FROM  Asteroid WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int

}