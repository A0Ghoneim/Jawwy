package com.example.jawwy.Favourites

import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.repo.IWeatherRepository
import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeRepository : IWeatherRepository {
    override fun getWeather(
        lat: Double,
        long: Double,
        units: String,
        language: String
    ): Flow<Response<JsonPojo>> {
        TODO("Not yet implemented")
    }

    override fun search(place: String, limit: Int): Flow<Response<SearchPojo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWeather(): Flow<List<JsonPojo>> = flow {
        emit(arrayListOf<JsonPojo>())
    }

    override suspend fun getAllAlerts(): Flow<List<AlertItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherById(id: String): Flow<JsonPojo> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(w: JsonPojo): Int {
        return 10
    }

    override suspend fun deleteFromGps(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insert(w: JsonPojo): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alertItem: AlertItem): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alertItem: AlertItem): Long {
        TODO("Not yet implemented")
    }

    override fun getNotificationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLocationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLatitude(): Double {
        TODO("Not yet implemented")
    }

    override fun getLongitude(): Double {
        TODO("Not yet implemented")
    }

    override fun getUnit(): String {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        TODO("Not yet implemented")
    }

    override fun putNotificationSettings(nkey: String) {
        TODO("Not yet implemented")
    }

    override fun putLocationSettings(key: String) {
        TODO("Not yet implemented")
    }

    override fun putLatitude(lat: Double) {
        TODO("Not yet implemented")
    }

    override fun putLongitude(long: Double) {
        TODO("Not yet implemented")
    }

    override fun putUnit(unit: String) {
        TODO("Not yet implemented")
    }

    override fun putLanguage(language: String) {
        TODO("Not yet implemented")
    }

}
