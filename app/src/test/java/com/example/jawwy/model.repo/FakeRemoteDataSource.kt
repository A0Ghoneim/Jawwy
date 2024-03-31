package com.example.jawwy.model.repo

import android.location.Address
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.searchdata.SearchPojo
import com.example.jawwy.network.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

class FakeRemoteDataSource (private val jsonPojo: JsonPojo?):IWeatherRemoteDataSource{
    override fun makeWeatherCall(
        lat: Double,
        long: Double,
        units: String,
        language: String
    ): Flow<Response<JsonPojo>> {
       if (jsonPojo?.lat == lat && jsonPojo?.lon == long){
           println("7asal")
           return flow { emit(Response.success(jsonPojo)) }
       }else{
           println("fashal")
           val TEXT = MediaType.parse("text/plain; charset=utf-8")
           return flow { emit(Response.error(400,ResponseBody.create(TEXT, "Hello, world!")))}
       }

    }

    override fun makeSearchCall(place: String, limit: Int): Flow<Response<SearchPojo>> {
        TODO("Not yet implemented")
    }

    override suspend fun makeAddressCall(lat: Double, long: Double): Address {
        TODO("Not yet implemented")
    }

}
