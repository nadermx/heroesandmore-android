package com.heroesandmore.app.data.dto.pricing

import com.google.gson.annotations.SerializedName

data class PriceHistoryPointDto(
    val date: String,
    @SerializedName("average_price")
    val averagePrice: String,
    @SerializedName("sale_count")
    val saleCount: Int
)
