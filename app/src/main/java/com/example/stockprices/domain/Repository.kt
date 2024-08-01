package com.example.stockprices.domain

interface Repository {

    suspend fun loadBarList():List<Bar>
}