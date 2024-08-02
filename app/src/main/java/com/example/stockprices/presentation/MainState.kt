package com.example.stockprices.presentation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.stockprices.domain.Bar
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class MainState(
    val bars: List<Bar>,
    val visibleBarCount: Int = 100,
    val screenWidth:Float=1f,
    var scrolledBy:Float=0f
): Parcelable {
    val barWidth:Float
        get()=screenWidth/visibleBarCount

    val visibleBars:List<Bar>
        get(){
            val startIndex=(scrolledBy/barWidth).roundToInt().coerceAtLeast(0)
            val endIndex=(startIndex+visibleBarCount).coerceAtMost(bars.size)
            return bars.subList(startIndex, endIndex)
        }
}

@Composable
fun rememberMainState(bars:List<Bar>):MutableState<MainState>{
    return rememberSaveable{
        mutableStateOf(MainState(bars))
    }
}