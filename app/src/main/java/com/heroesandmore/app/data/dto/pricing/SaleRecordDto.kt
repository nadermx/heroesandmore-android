package com.heroesandmore.app.data.dto.pricing

import com.google.gson.annotations.SerializedName

data class SaleRecordDto(
    val id: Int,
    val source: String,
    @SerializedName("sale_price")
    val salePrice: String,
    @SerializedName("sale_date")
    val saleDate: String,
    val condition: String?,
    val grade: String?,
    @SerializedName("grading_company")
    val gradingCompany: String?,
    val platform: String?
)
