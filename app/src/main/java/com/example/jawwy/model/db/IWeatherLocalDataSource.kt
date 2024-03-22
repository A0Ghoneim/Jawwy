package com.example.jawwy.model.db

import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun getWeatherById(id:String): Flow<JsonPojo>

    suspend fun delete(w: JsonPojo):Int

    suspend fun insert(w: JsonPojo):Long
}