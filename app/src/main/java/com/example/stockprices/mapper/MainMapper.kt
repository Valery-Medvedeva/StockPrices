package com.example.stockprices.mapper

import com.example.stockprices.data.model.BarDto
import com.example.stockprices.domain.Bar
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
}