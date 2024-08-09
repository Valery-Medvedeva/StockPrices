package com.example.stockprices.data.network

import com.example.stockprices.data.model.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(
        "aggs/ticker/AAPL/range/{timeframe}/2022-01-09/2023-01-09?" +
                "adjusted=true&sort=desc&limit=50000&apiKey=NwkhSdRWbq1sRM8ofC3y8noTd5vpO4RN"
    )
    suspend fun loadBars(
        @Path("timeframe") timeFrameDto: String,
    ): ResponseDto

}