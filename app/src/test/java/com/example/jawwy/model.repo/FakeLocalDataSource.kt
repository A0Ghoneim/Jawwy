package com.example.jawwy.model.repo

import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.IWeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(private val list: MutableList<JsonPojo>?):IWeatherLocalDataSource {
    override suspend fun getAllWeather(): Flow<List<JsonPojo>> {
        if (list !=null){
            return flow { emit(list) }
        }else{
            return flow { emit(emptyList()) }
        }
    }

    override suspend fun getAllAlerts(): Flow<List<AlertItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherById(id: String): Flow<JsonPojo> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(w: JsonPojo): Int {
        if (list?.remove(w) == true){
            return 1
        }else{
            return 0
        }
    }

    override suspend fun deleteFromGps(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insert(w: JsonPojo): Long {
        if (list?.add(w)==true){
            return 1L
        }else{
            return 0L
        }
    }

    override suspend fun deleteAlert(a: AlertItem): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(a: AlertItem): Long {
        TODO("Not yet implemented")
    }

}
