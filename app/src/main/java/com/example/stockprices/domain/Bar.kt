package com.example.stockprices.domain

import android.icu.util.Calendar
import java.util.Date

data class Bar (
    val openPrice:Float,
    val closePrice:Float,
    val highestPrice:Float,
    val lowestPrice:Float,
    val openingTime:Long
) {
    val calendar: Calendar
        get() {
            return Calendar.getInstance().apply {
                time = Date(openingTime)
            }
        }
}