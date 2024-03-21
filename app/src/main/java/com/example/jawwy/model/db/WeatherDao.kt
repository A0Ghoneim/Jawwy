package com.example.jawwy.model.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.Flow

interface WeatherDao {
        @Query("Select * from Weather where lat =:latitude and long = :longitude")
        fun getWeatherByLocation(latitude:Double,longitude:Double): Flow<JsonPojo>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertWeather(w: JsonPojo) : Long

        @Delete
        suspend fun deleteWeather(w: JsonPojo) : Int

}