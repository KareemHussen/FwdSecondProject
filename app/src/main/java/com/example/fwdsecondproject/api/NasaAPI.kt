package com.example.fwdsecondproject.api

import com.example.fwdsecondproject.Constants
import com.example.fwdsecondproject.models.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaAPI {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") startDate:String,
        @Query("api_key") apiKey : String = Constants.API_KEY
    ) : Deferred<ResponseBody>

    @GET("planetary/apod")
    fun getPictureOfDay(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<PictureOfDay>


}