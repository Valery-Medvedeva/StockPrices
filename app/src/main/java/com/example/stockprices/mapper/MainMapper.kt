package com.example.stockprices.mapper

import com.example.stockprices.data.model.BarDto
import com.example.stockprices.data.model.TimeFrameDto
import com.example.stockprices.domain.Bar
import com.example.stockprices.domain.TimeFrame
import javax.inject.Inject

class MainMapper @Inject constructor(){

    fun mapResponseToBarList(dtoList: List<BarDto>):List<Bar>{
        val newList= mutableListOf<Bar>()
        for (barDto in dtoList){
            val bar=Bar(
                openPrice = barDto.openPrice,
                closePrice = barDto.closePrice,
                highestPrice = barDto.highestPrice,
                lowestPrice = barDto.lowestPrice,
                openingTime = barDto.openingTime
            )
            newList.add(bar)
        }
        return newList
    }

    fun mapTimeFrameToTimeFrameDto(timeFrame: TimeFrame): TimeFrameDto =
        when (timeFrame){
            TimeFrame.HOUR_1->TimeFrameDto.HOUR_1
            TimeFrame.MIN_5->TimeFrameDto.MIN_5
            TimeFrame.MIN_15->TimeFrameDto.MIN_15
            TimeFrame.MIN_30->TimeFrameDto.MIN_30
    }
}