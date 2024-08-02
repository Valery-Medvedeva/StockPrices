package com.example.stockprices.domain

import android.icu.util.Calendar
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Bar (
    val openPrice:Float,
    val closePrice:Float,
    val highestPrice:Float,
    val lowestPrice:Float,
    val openingTime:Long
): Parcelable {
    val calendar: Calendar
        get() {
            return Calendar.getInstance().apply {
                time = Date(openingTime)
            }
        }
}