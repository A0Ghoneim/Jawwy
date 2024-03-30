package com.example.jawwy.network

import android.location.Address
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IWeatherRemoteDataSource {
    fun makeWeatherCall(lat : Double,long : Double,units: String = "standard",language: String = "en"): Flow<Response<JsonPojo>>
    fun makeSearchCall(place : String,limit : Int): Flow<Response<SearchPojo>>

}