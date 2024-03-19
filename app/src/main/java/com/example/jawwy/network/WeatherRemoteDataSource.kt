package com.example.jawwy.network

import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRemoteDataSource: IWeatherRemoteDataSource {
    val retrofitService : WeatherService by lazy {
        RetrofitHelper.retrofit.create(WeatherService::class.java)
    }
    val searchRetrofitService : SearchService by lazy {
        SearchRetrofitHelper.retrofit.create(SearchService::class.java)
    }
    override fun makeWeatherCall(
        lat: Double,
        long: Double,
        units: String,
        language: String
    ): Flow<Response<JsonPojo>> = flow {
        emit(retrofitService.getWeatherData(lat,long,units,language))
    }

    override fun makeSearchCall(place: String,limit : Int): Flow<Response<SearchPojo>> = flow {
        emit(searchRetrofitService.searchLocation(place,limit))
    }
}