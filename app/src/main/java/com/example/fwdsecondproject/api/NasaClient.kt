package com.example.fwdsecondproject.api

import com.example.fwdsecondproject.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NasaClient {

     private val client = OkHttpClient()
          .newBuilder()
          .readTimeout(60 , TimeUnit.SECONDS)
          .connectTimeout(60 , TimeUnit.SECONDS)
          .build()

     private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

     private val retrofit = Retrofit.Builder()
          .baseUrl(Constants.BASE_URL)
          .addConverterFactory(MoshiConverterFactory.create(moshi))
          .addCallAdapterFactory(CoroutineCallAdapterFactory())
          .client(client)
          .build()

     val api = retrofit.create(NasaAPI::class.java)




}