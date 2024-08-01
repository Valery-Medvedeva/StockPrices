package com.example.stockprices.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.stockprices.domain.Bar

@Composable
fun MainScreen(bars:List<Bar>){
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val maxPrice=bars.maxOf { it.highestPrice }
        val minPrice=bars.minOf { it.highestPrice }
        val barWidth=size.width/bars.size
        val pxPerPoint = size.height/(maxPrice-minPrice)
        bars.forEachIndexed{ index, bar->
            val offsetX=index*barWidth
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height-(bar.lowestPrice-minPrice)*pxPerPoint),
                end = Offset(offsetX, size.height-(bar.highestPrice-minPrice)*pxPerPoint),
                strokeWidth = 2f
            )
        }
    }
}