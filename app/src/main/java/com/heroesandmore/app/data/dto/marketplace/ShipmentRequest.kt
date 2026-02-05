package com.heroesandmore.app.data.dto.marketplace

import com.google.gson.annotations.SerializedName

data class ShipmentRequest(
    @SerializedName("tracking_number")
    val trackingNumber: String,
    @SerializedName("tracking_carrier")
    val trackingCarrier: String
)
