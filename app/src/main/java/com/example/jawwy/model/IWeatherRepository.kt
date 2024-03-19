package com.example.jawwy.model

import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IWeatherRepository {
    fun getWeather(
        lat: Double,
        long: Double,
        units: String="metric",
        language: String ="en"
    ): Flow<Response<JsonPojo>>

    fun search(place :String,limit : Int):Flow<Response<SearchPojo>>
}
