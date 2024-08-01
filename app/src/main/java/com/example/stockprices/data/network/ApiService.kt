package com.example.stockprices.data.network

import com.example.stockprices.data.model.ResponseDto
import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/hour/2022-01-09/2023-01-09?" +
         "adjusted=true&sort=asc&limit=50000&apiKey=NwkhSdRWbq1sRM8ofC3y8noTd5vpO4RN")
    suspend fun loadBars(): ResponseDto

}