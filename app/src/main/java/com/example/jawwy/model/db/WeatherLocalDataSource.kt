package com.example.jawwy.model.db

import android.content.Context
import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource private constructor(val context: Context):IWeatherLocalDataSource {

    val myDAO by lazy {
        AppDataBase.getInstance(context).weatherDao()
    }
    companion object {
        @Volatile
        private var Instance: IWeatherLocalDataSource? = null
        @Synchronized
        fun getInstance(context: Context): IWeatherLocalDataSource {
            return Instance ?: synchronized(this){
                val  instance = WeatherLocalDataSource(context)
                Instance=instance
                instance
            }
        }
    }

    override suspend fun getAllWeather(): Flow<List<JsonPojo>> {
        return myDAO.getAllWeather()
    }

    override suspend fun getAllAlerts(): Flow<List<AlertItem>> {
        return myDAO.getAllAlerts()
    }

    override suspend fun getWeatherById(id:String): Flow<JsonPojo> {
        return myDAO.getWeatherById(id)
    }

    override suspend fun delete(w: JsonPojo): Int {
        return myDAO.deleteWeather(w)
    }

    override suspend fun deleteFromGps(): Int {
        return myDAO.deleteFromGps()
    }

    override suspend fun insert(w: JsonPojo): Long {
        return myDAO.insertWeather(w)
    }

    override suspend fun deleteAlert(a: AlertItem): Int {
        return myDAO.deleteAlert(a)
    }

    override suspend fun insertAlert(a: AlertItem): Long {
        return myDAO.insertAlert(a)
    }
}