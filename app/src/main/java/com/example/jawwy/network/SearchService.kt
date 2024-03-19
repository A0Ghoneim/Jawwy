package com.example.jawwy.network

import com.example.jawwy.model.searchdata.SearchPojo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("api/")
    suspend fun searchLocation(@Query("q") query: String, @Query("limit") limit: Int): Response<SearchPojo>
}