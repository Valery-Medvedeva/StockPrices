package com.example.stockprices.presentation

import com.example.stockprices.domain.Bar

sealed class MainState {

    data object Initial:MainState()

    data class Content(val barList:List<Bar>) : MainState()
}