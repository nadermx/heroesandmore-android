package com.heroesandmore.app.data.dto.collections

import com.google.gson.annotations.SerializedName

data class CollectionValueSnapshotDto(
    val date: String,
    @SerializedName("total_value")
    val totalValue: String,
    @SerializedName("item_count")
    val itemCount: Int
)
