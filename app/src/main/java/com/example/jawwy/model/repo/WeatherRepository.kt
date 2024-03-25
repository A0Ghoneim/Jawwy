package com.example.jawwy.model.repo

import com.example.jawwy.alert.AlertItem
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.IWeatherLocalDataSource
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.searchdata.SearchPojo
import com.example.jawwy.model.sharedprefrence.ISharedPreferenceDatasource
import com.example.jawwy.model.sharedprefrence.SharedPreferenceDatasource
import com.example.jawwy.network.IWeatherRemoteDataSource
import com.example.jawwy.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepository(val weatherRemoteDataSource: IWeatherRemoteDataSource, val weatherLocalDataSource: IWeatherLocalDataSource, val sharedPreferenceDatasource: ISharedPreferenceDatasource):
    IWeatherRepository {
    override fun getWeather(lat: Double, long: Double, units: String, language: String): Flow<Response<JsonPojo>> {
        return weatherRemoteDataSource.makeWeatherCall(lat,long,units,language)
    }

    override fun search(place: String,limit : Int): Flow<Response<SearchPojo>> {
        return weatherRemoteDataSource.makeSearchCall(place,limit)
    }

    override suspend fun getAllWeather(): Flow<List<JsonPojo>> {
        return weatherLocalDataSource.getAllWeather()
    }

    override suspend fun getAllAlerts(): Flow<List<AlertItem>> {
        return weatherLocalDataSource.getAllAlerts()
    }

    override suspend fun getWeatherById(id:String): Flow<JsonPojo> {
        return weatherLocalDataSource.getWeatherById(id)
    }

    override suspend fun delete(w: JsonPojo): Int {
        return weatherLocalDataSource.delete(w)
    }

    override suspend fun insert(w: JsonPojo): Long {
        return weatherLocalDataSource.insert(w)
    }

    override suspend fun deleteFromGps(): Int {
        return weatherLocalDataSource.deleteFromGps()
    }

    override suspend fun deleteAlert(alertItem: AlertItem): Int {
        return weatherLocalDataSource.deleteAlert(alertItem)
    }

    override suspend fun insertAlert(alertItem: AlertItem): Long {
        return weatherLocalDataSource.insertAlert(alertItem)
    }

    override fun getLocationSettings(): String {
        return sharedPreferenceDatasource.getLocationSettings()
    }

    override fun getLatitude(): Double {
        return sharedPreferenceDatasource.getLatitude()
    }

    override fun getLongitude(): Double {
        return sharedPreferenceDatasource.getLongitude()
    }

    override fun getUnit(): String {
        return sharedPreferenceDatasource.getUnit()
    }

    override fun getLanguage(): String {
        return sharedPreferenceDatasource.getLanguage()
    }

    override fun putLocationSettings(key: String) {
        return sharedPreferenceDatasource.putLocationSettings(key)
    }

    override fun putLatitude(lat: Double) {
        return sharedPreferenceDatasource.putLatitude(lat)
    }

    override fun putLongitude(long: Double) {
        return sharedPreferenceDatasource.putLongitude(long)
    }

    override fun putUnit(unit: String) {
        return sharedPreferenceDatasource.putUnit(unit)
    }

    override fun putLanguage(language: String) {
        return sharedPreferenceDatasource.putLanguage(language)
    }

}