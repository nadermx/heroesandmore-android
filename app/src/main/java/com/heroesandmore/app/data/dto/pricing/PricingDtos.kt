package com.heroesandmore.app.data.dto.pricing

import com.google.gson.annotations.SerializedName

data class PriceGuideItemDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_slug")
    val categorySlug: String,
    @SerializedName("primary_image")
    val primaryImage: String?,
    val year: Int?,
    val set: String?,
    @SerializedName("card_number")
    val cardNumber: String?,
    @SerializedName("average_price")
    val averagePrice: String?,
    @SerializedName("lowest_price")
    val lowestPrice: String?,
    @SerializedName("highest_price")
    val highestPrice: String?,
    @SerializedName("last_sale_price")
    val lastSalePrice: String?,
    @SerializedName("sale_count")
    val saleCount: Int
)

data class PriceGuideItemDetailDto(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_slug")
    val categorySlug: String,
    val images: List<String>,
    val year: Int?,
    val set: String?,
    @SerializedName("card_number")
    val cardNumber: String?,
    val attributes: Map<String, String>?,
    @SerializedName("average_price")
    val averagePrice: String?,
    @SerializedName("lowest_price")
    val lowestPrice: String?,
    @SerializedName("highest_price")
    val highestPrice: String?,
    @SerializedName("last_sale_price")
    val lastSalePrice: String?,
    @SerializedName("sale_count")
    val saleCount: Int,
    @SerializedName("active_listings_count")
    val activeListingsCount: Int
)

data class GradePriceDto(
    val grade: String,
    @SerializedName("grading_company")
    val gradingCompany: String?,
    @SerializedName("average_price")
    val averagePrice: String?,
    @SerializedName("lowest_price")
    val lowestPrice: String?,
    @SerializedName("highest_price")
    val highestPrice: String?,
    @SerializedName("sale_count")
    val saleCount: Int
)

data class SaleDto(
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

data class PriceHistoryDto(
    val date: String,
    @SerializedName("average_price")
    val averagePrice: String,
    @SerializedName("sale_count")
    val saleCount: Int
)

data class TrendingItemDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("primary_image")
    val primaryImage: String?,
    @SerializedName("current_price")
    val currentPrice: String?,
    @SerializedName("price_change")
    val priceChange: String?,
    @SerializedName("price_change_percent")
    val priceChangePercent: String?,
    @SerializedName("trend_direction")
    val trendDirection: String
)

data class PriceGuideCategoryDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("item_count")
    val itemCount: Int,
    val children: List<PriceGuideCategoryDto>?
)
