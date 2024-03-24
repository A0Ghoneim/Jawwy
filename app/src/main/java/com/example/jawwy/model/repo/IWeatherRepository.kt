package com.example.jawwy.model.repo

import com.example.jawwy.alert.AlertItem
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
    suspend fun getAllWeather(): Flow<List<JsonPojo>>
    suspend fun getAllAlerts(): Flow<List<AlertItem>>

    suspend fun getWeatherById(id: String): Flow<JsonPojo>

    suspend fun delete(w: JsonPojo):Int

    suspend fun insert(w: JsonPojo):Long

    suspend fun deleteAlert(alertItem: AlertItem):Int

    suspend fun insertAlert(alertItem: AlertItem):Long


    fun getKey():String
    fun getLatitude():Double
    fun getLongitude():Double
    fun getUnit():String
    fun getLanguage():String

    fun putKey(key:String)
    fun putLatitude(lat:Double)
    fun putLongitude(long:Double)
    fun putUnit(unit: String)
    fun putLanguage(language: String)
}
