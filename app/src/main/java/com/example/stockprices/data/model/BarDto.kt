package com.example.stockprices.data.model

import com.google.gson.annotations.SerializedName

data class BarDto(
    @SerializedName("o") val openPrice:Float,
    @SerializedName("c") val closePrice:Float,
    @SerializedName("h") val highestPrice:Float,
    @SerializedName("l") val lowestPrice:Float,
    @SerializedName("t") val openingTime:Long
)
