package com.example.stockprices.domain

import javax.inject.Inject

class LoadBarListUseCase @Inject constructor(
    private val repository: Repository,
) {
    suspend fun loadBars(timeFrame: TimeFrame)=repository.loadBarList(timeFrame = timeFrame)
}