package com.example.jawwy.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val BASE_URL2 = "https://photon.komoot.io/"
object SearchRetrofitHelper {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL2)
        .build()
}