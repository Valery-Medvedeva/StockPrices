package com.example.stockprices.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import com.example.stockprices.domain.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT=20

@Composable
fun MainScreen(bars:List<Bar>){

    var mainState by rememberMainState(bars = bars)

    val transformableState= TransformableState { zoomChange, panChange, _ ->
        val visibleBarCount=(mainState.visibleBarCount/zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT,bars.size)

        val scrolledBy = (mainState.scrolledBy+panChange.x)
            .coerceIn(0f, mainState.barWidth*bars.size-mainState.screenWidth)

        mainState=mainState.copy(
            visibleBarCount = visibleBarCount,
            scrolledBy = scrolledBy
        )
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState)
            .onSizeChanged {
                mainState = mainState.copy(screenWidth = it.width.toFloat())
            }
    ) {
        val maxPrice=mainState.visibleBars.maxOf { it.highestPrice }
        val minPrice=mainState.visibleBars.minOf { it.highestPrice }
        val pxPerPoint = size.height/(maxPrice-minPrice)
        translate(left=mainState.scrolledBy){
            bars.forEachIndexed{ index, bar->
                val offsetX=size.width-index*mainState.barWidth
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height-(bar.lowestPrice-minPrice)*pxPerPoint),
                    end = Offset(offsetX, size.height-(bar.highestPrice-minPrice)*pxPerPoint),
                    strokeWidth = 2f
                )
                drawLine(
                    color = if (bar.openPrice>bar.closePrice) Color.Red else Color.Green,
                    start = Offset(offsetX, size.height-(bar.openPrice-minPrice)*pxPerPoint),
                    end = Offset(offsetX, size.height-(bar.closePrice-minPrice)*pxPerPoint),
                    strokeWidth = mainState.barWidth/2
                )
            }
        }
    }
}