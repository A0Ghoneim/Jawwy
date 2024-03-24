package com.example.jawwy.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.Alerts
import com.example.jawwy.model.data.Current
import com.example.jawwy.model.data.Daily
import com.example.jawwy.model.data.FeelsLike
import com.example.jawwy.model.data.Hourly
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.data.Temp
import com.example.jawwy.model.data.Weather

@Database(entities = [JsonPojo::class,Current::class,Daily::class,Hourly::class,Alerts::class,FeelsLike::class,Temp::class,Weather::class,AlertItem::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    companion object {
        @Volatile
        private var Instance: AppDataBase? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            return Instance ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "Weather"
                ).build()
                Instance=instance
                instance
            }
        }
    }
}