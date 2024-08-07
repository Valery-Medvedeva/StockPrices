package com.example.stockprices.domain

interface Repository {

    suspend fun loadBarList(timeFrame: TimeFrame):List<Bar>
}