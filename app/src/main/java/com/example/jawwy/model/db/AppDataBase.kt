package com.example.jawwy.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jawwy.model.data.JsonPojo

@Database(entities = [JsonPojo::class], version = 1)
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