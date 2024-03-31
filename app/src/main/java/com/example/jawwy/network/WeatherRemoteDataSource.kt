package com.example.jawwy.network

import android.content.Context
import android.location.Address
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.db.IWeatherLocalDataSource
import com.example.jawwy.model.db.WeatherLocalDataSource
import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRemoteDataSource private constructor(context: Context): IWeatherRemoteDataSource {
    private val retrofitService : WeatherService by lazy {
        RetrofitHelper.retrofit.create(WeatherService::class.java)
    }
    private val searchRetrofitService : SearchService by lazy {
        SearchRetrofitHelper.retrofit.create(SearchService::class.java)
    }
    private val addressService : AddressService by lazy {
        AddressService(context)
    }

    companion object {
        @Volatile
        private var Instance: IWeatherRemoteDataSource? = null
        @Synchronized
        fun getInstance(context: Context): IWeatherRemoteDataSource {
            return Instance ?: synchronized(this){
                val  instance = WeatherRemoteDataSource(context)
                Instance=instance
                instance
            }
        }
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
        emit(searchRetrofitService.searchLocation(place,limit,"en"))
    }

    override suspend fun makeAddressCall(lat: Double, long: Double): Address {
        return addressService.getAddress(lat, long)
    }

}