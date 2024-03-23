package com.example.jawwy.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.Flow
@Dao
interface WeatherDao {
        @Query("Select * from JsonPojo")
        fun getAllWeather(): Flow<List<JsonPojo>>
        @Query("Select * from JsonPojo where id =:id")
        fun getWeatherById(id:String): Flow<JsonPojo>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertWeather(w: JsonPojo) : Long

        @Delete
        suspend fun deleteWeather(w: JsonPojo) : Int

}