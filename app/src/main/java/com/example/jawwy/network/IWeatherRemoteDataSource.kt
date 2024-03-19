package com.example.jawwy.network

import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IWeatherRemoteDataSource {
    fun makeWeatherCall(lat : Double,long : Double,units: String = "metric",language: String = "en"): Flow<Response<JsonPojo>>
    fun makeSearchCall(place : String,limit : Int): Flow<Response<SearchPojo>>
}