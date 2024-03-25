package com.example.jawwy.model.db

import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {

    suspend fun getAllWeather(): Flow<List<JsonPojo>>
    suspend fun getAllAlerts(): Flow<List<AlertItem>>

    suspend fun getWeatherById(id:String): Flow<JsonPojo>

    suspend fun delete(w: JsonPojo):Int

    suspend fun deleteFromGps():Int

    suspend fun insert(w: JsonPojo):Long

    suspend fun deleteAlert(a: AlertItem):Int

    suspend fun insertAlert(a: AlertItem):Long
}