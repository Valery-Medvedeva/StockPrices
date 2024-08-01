package com.example.stockprices.domain

import javax.inject.Inject

class LoadBarListUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke()=repository.loadBarList()
}