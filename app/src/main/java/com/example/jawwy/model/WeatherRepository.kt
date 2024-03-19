package com.example.jawwy.model

import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.searchdata.SearchPojo
import com.example.jawwy.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepository(val weatherRemoteDataSource: WeatherRemoteDataSource):IWeatherRepository {
    override fun getWeather(lat: Double, long: Double, units: String, language: String): Flow<Response<JsonPojo>> {
        return weatherRemoteDataSource.makeWeatherCall(lat,long,units,language)
    }

    override fun search(place: String,limit : Int): Flow<Response<SearchPojo>> {
        return weatherRemoteDataSource.makeSearchCall(place,limit)
    }
}