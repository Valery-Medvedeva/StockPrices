package com.example.stockprices.presentation
import com.example.stockprices.domain.Bar
import com.example.stockprices.domain.TimeFrame

sealed class MainScreenState {

    data object Initial:MainScreenState()

    data object Loading:MainScreenState()

    data object Error: MainScreenState()

    data class Content(val barList:List<Bar>, val timeFrame: TimeFrame) : MainScreenState()
}