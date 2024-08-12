package com.example.stockprices.presentation

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockprices.R
import com.example.stockprices.domain.Bar
import com.example.stockprices.domain.TimeFrame
import com.example.stockprices.getMainComponent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val component = getMainComponent()
    val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.state.collectAsState(initial = MainScreenState.Initial)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        when (val currentState = screenState.value) {
            is MainScreenState.Initial -> {}
            is MainScreenState.Content -> {
                LaunchedEffect(currentState.sideEffect) {
                    when (currentState.sideEffect) {
                        Event.Error -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Too many attempts")
                            }
                        }
                        null -> Unit
                    }
                }
                val mainState = rememberMainState(bars = currentState.barList)
                Content(
                    modifier = modifier,
                    mainState = mainState,
                    onMainStateChanged = {
                        mainState.value = it
                    },
                    timeFrame = currentState.timeFrame
                )
                TimeFrames(selectedFrame = currentState.timeFrame) {
                    viewModel.loadBarList(it)
                }
            }

            is MainScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is MainScreenState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Check your internet connection")
                }
                Log.d("MainScreen", "something goes wrong")
            }
        }
    }
}

private fun DrawScope.drawTimeDelimiter(
    bar: Bar,
    nextBar: Bar?,
    timeFrame: TimeFrame,
    offsetX: Float,
    textMeasurer: TextMeasurer,
) {
    val calendar = bar.calendar
    val minutes = calendar.get(Calendar.MINUTE)
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val nameOfMonth = calendar.get(Calendar.MONTH)
    val shouldDrawTimeDelimiter = when (timeFrame) {
        TimeFrame.MIN_5 -> {
            minutes == 0
        }

        TimeFrame.MIN_15 -> {
            minutes == 0 && hours % 2 == 0
        }

        TimeFrame.MIN_30, TimeFrame.HOUR_1 -> {
            val nextBarDay = nextBar?.calendar?.get(Calendar.DAY_OF_MONTH)
            day != nextBarDay
        }
    }
    if (!shouldDrawTimeDelimiter) return
    drawLine(
        color = Color.White.copy(alpha = 0.5f),
        start = Offset(offsetX, 0f),
        end = Offset(offsetX, size.height),
        strokeWidth = 1f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
    )
    val text = when (timeFrame) {
        TimeFrame.MIN_5, TimeFrame.MIN_15 -> {
            String.format("%02d:00", hours)
        }

        TimeFrame.MIN_30, TimeFrame.HOUR_1 -> {
            String.format("%s %s ", day, nameOfMonth)
        }
    }
    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(offsetX - textLayoutResult.size.width / 2, size.height)
    )
}

@Composable
private fun TimeFrames(
    selectedFrame: TimeFrame,
    onTimeFrameSelected: (TimeFrame) -> Unit,
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TimeFrame.entries.forEach { timeFrame ->
            val labelResId = when (timeFrame) {
                TimeFrame.MIN_5 -> R.string.timeframe_5_minutes
                TimeFrame.MIN_15 -> R.string.timeframe_15_minutes
                TimeFrame.MIN_30 -> R.string.timeframe_30_minutes
                TimeFrame.HOUR_1 -> R.string.timeframe_1_hour
            }
            val isSelected = timeFrame == selectedFrame
            AssistChip(
                onClick = { onTimeFrameSelected(timeFrame) },
                label = { Text(text = stringResource(id = labelResId)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) Color.White else Color.Black,
                    labelColor = if (isSelected) Color.Black else Color.White
                )
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    mainState: State<MainState>,
    onMainStateChanged: (MainState) -> Unit,
    timeFrame: TimeFrame,
) {
    val currentState = mainState.value
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarCount = (currentState.visibleBarCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, currentState.bars.size)

        val scrolledBy = (currentState.scrolledBy + panChange.x)
            .coerceIn(0f, currentState.barWidth * currentState.bars.size - currentState.screenWidth)

        onMainStateChanged(
            currentState.copy(
                visibleBarCount = visibleBarCount,
                scrolledBy = scrolledBy
            )
        )
    }
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clipToBounds()
            .padding(top = 32.dp, bottom = 32.dp)
            .transformable(transformableState)
            .onSizeChanged {
                onMainStateChanged(
                    currentState.copy(
                        screenWidth = it.width.toFloat(),
                        screenHeight = it.height.toFloat()
                    )
                )
            }
    ) {
        val maxPrice = currentState.maxPrice
        val minPrice = currentState.minPrice
        val pxPerPoint = currentState.pxPerPoint
        translate(left = currentState.scrolledBy) {
            currentState.bars.forEachIndexed { index, bar ->
                val offsetX = size.width - index * currentState.barWidth
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
                    strokeWidth = currentState.barWidth / 2
                )
                drawTimeDelimiter(
                    bar = bar,
                    nextBar = if (index < currentState.bars.size - 1) {
                        currentState.bars[index + 1]
                    } else {
                        null
                    },
                    timeFrame = timeFrame,
                    offsetX = offsetX,
                    textMeasurer = textMeasurer
                )
            }
        }
        drawPrices(
            min = minPrice,
            pxPerPoint = pxPerPoint,
            lastPrice = currentState.bars.first().closePrice,
            textMeasurer = textMeasurer,
            max = maxPrice
        )
    }
}

private fun DrawScope.drawPrices(
    min: Float,
    max: Float,
    pxPerPoint: Float,
    lastPrice: Float,
    textMeasurer: TextMeasurer,
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
        end = Offset(size.width, size.height - (lastPrice - min) * pxPerPoint),
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
    color: Color = Color.White,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 1f,
    textMeasurer: TextMeasurer,
    price: Float,
) {
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
        text = price.toString(),
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(size.width - textLayoutResult.size.width - 4.dp.toPx(), end.y)
    )
}

