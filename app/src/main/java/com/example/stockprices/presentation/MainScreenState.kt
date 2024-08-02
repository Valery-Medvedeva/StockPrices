package com.example.stockprices.presentation

import com.example.stockprices.domain.Bar

sealed class MainScreenState {

    data object Initial:MainScreenState()

    data class Content(val barList:List<Bar>) : MainScreenState()
}