package com.example.stockprices.data

import com.example.stockprices.data.network.ApiService
import com.example.stockprices.domain.Bar
import com.example.stockprices.domain.Repository
import com.example.stockprices.domain.TimeFrame
import com.example.stockprices.mapper.MainMapper
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: MainMapper,
) : Repository {

    override suspend fun loadBarList(timeFrame: TimeFrame): List<Bar> {
        val tfDto = mapper.mapTimeFrameToTimeFrameDto(timeFrame)
        return mapper.mapResponseToBarList(apiService.loadBars(tfDto.value).barList)
    }
}