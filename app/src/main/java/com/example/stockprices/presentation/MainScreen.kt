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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.stockprices.domain.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT=20

@Composable
fun MainScreen(bars:List<Bar>){
    var visibleBarCount by remember {
        mutableStateOf(100)
    }
    val transformableState= TransformableState { zoomChange, _, _ ->
        visibleBarCount=(visibleBarCount/zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT,bars.size)
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState)
    ) {
        val maxPrice=bars.maxOf { it.highestPrice }
        val minPrice=bars.minOf { it.highestPrice }
        val barWidth=size.width/visibleBarCount
        val pxPerPoint = size.height/(maxPrice-minPrice)
        bars.take(visibleBarCount).forEachIndexed{ index, bar->
            val offsetX=size.width-index*barWidth
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height-(bar.lowestPrice-minPrice)*pxPerPoint),
                end = Offset(offsetX, size.height-(bar.highestPrice-minPrice)*pxPerPoint),
                strokeWidth = 2f
            )
        }
    }
}