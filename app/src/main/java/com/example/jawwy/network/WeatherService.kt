package com.example.jawwy.network

import com.example.jawwy.model.data.JsonPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/3.0/onecall")
   suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String = "dbb36dba7836220d4e5291f0261fa0f5"
    ): Response<JsonPojo>
}