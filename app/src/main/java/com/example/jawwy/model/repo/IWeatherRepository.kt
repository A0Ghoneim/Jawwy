package com.example.jawwy.model.repo

import android.location.Address
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
    suspend fun getAddress(lat: Double,long: Double):Address


    fun search(place :String,limit : Int):Flow<Response<SearchPojo>>
    suspend fun getAllWeather(): Flow<List<JsonPojo>>
    suspend fun getAllAlerts(): Flow<List<AlertItem>>

    suspend fun getWeatherById(id: String): Flow<JsonPojo>


    suspend fun delete(w: JsonPojo):Int

    suspend fun deleteFromGps():Int

    suspend fun insert(w: JsonPojo):Long

    suspend fun deleteAlert(alertItem: AlertItem):Int

    suspend fun insertAlert(alertItem: AlertItem):Long

    fun getNotificationSettings():String
    fun getLocationSettings():String
    fun getLatitude():Double
    fun getLongitude():Double
    fun getUnit():String
    fun getLanguage():String

    fun putNotificationSettings(nkey:String)
    fun putLocationSettings(key:String)
    fun putLatitude(lat:Double)
    fun putLongitude(long:Double)
    fun putUnit(unit: String)
    fun putLanguage(language: String)
}
