package com.example.stockprices.data.model

import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("results") val barList:List<BarDto>
)
