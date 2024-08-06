package com.example.stockprices.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockprices.domain.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun MainScreen(bars: List<Bar>) {

    var mainState by rememberMainState(bars = bars)

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        val visibleBarCount = (mainState.visibleBarCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)

        val scrolledBy = (mainState.scrolledBy + panChange.x)
            .coerceIn(0f, mainState.barWidth * bars.size - mainState.screenWidth)

        mainState = mainState.copy(
            visibleBarCount = visibleBarCount,
            scrolledBy = scrolledBy
        )
    }
    val textMeasurer= rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 32.dp, bottom = 32.dp)
            .transformable(transformableState)
            .onSizeChanged {
                mainState = mainState.copy(screenWidth = it.width.toFloat())
            }
    ) {
        val maxPrice = mainState.visibleBars.maxOf { it.highestPrice }
        val minPrice = mainState.visibleBars.minOf { it.highestPrice }
        val pxPerPoint = size.height / (maxPrice - minPrice)
        translate(left = mainState.scrolledBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - index * mainState.barWidth
                drawLine(
                    color = Color.White,
                    start = Offset(
                        offsetX,
                        size.height - (bar.lowestPrice - minPrice) * pxPerPoint
                    ),
                    end = Offset(offsetX, size.height - (bar.highestPrice - minPrice) * pxPerPoint),
                    strokeWidth = 2f
                )
                drawLine(
                    color = if (bar.openPrice > bar.closePrice) Color.Red else Color.Green,
                    start = Offset(offsetX, size.height - (bar.openPrice - minPrice) * pxPerPoint),
                    end = Offset(offsetX, size.height - (bar.closePrice - minPrice) * pxPerPoint),
                    strokeWidth = mainState.barWidth / 2
                )
            }
        }
            drawPrices(
                min = minPrice,
                pxPerPoint = pxPerPoint,
                lastPrice = bars.first().closePrice,
                textMeasurer = textMeasurer,
                max=maxPrice
            )
    }
}

private fun DrawScope.drawPrices(
    min: Float,
    max:Float,
    pxPerPoint: Float,
    lastPrice:Float,
    textMeasurer: TextMeasurer
) {
    //max
    drawDashedLineWithText(
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
        textMeasurer = textMeasurer,
        price = max
    )
    //lastPrice
    drawDashedLineWithText(
        start = Offset(0f, size.height - (lastPrice - min) * pxPerPoint),
        end = Offset(size.width, size.height -(lastPrice - min) * pxPerPoint),
        textMeasurer = textMeasurer,
        price = lastPrice
    )
    //min
    drawDashedLineWithText(
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        textMeasurer = textMeasurer,
        price = min
    )
}

private fun DrawScope.drawDashedLineWithText(
    color:Color=Color.White,
    start:Offset,
    end:Offset,
    strokeWidth:Float = 1f,
    textMeasurer: TextMeasurer,
    price:Float
){
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )
    val textLayoutResult = textMeasurer.measure(
        text=price.toString(),
        style = TextStyle(
            color=Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(size.width-textLayoutResult.size.width,end.y))
}

