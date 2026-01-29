package com.heroesandmore.app.domain.model

data class PriceGuideItem(
    val id: Int,
    val name: String,
    val slug: String,
    val categoryName: String,
    val categorySlug: String,
    val primaryImage: String?,
    val year: Int?,
    val set: String?,
    val cardNumber: String?,
    val averagePrice: String?,
    val lowestPrice: String?,
    val highestPrice: String?,
    val lastSalePrice: String?,
    val saleCount: Int
)

data class PriceGuideItemDetail(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val categoryName: String,
    val categorySlug: String,
    val images: List<String>,
    val year: Int?,
    val set: String?,
    val cardNumber: String?,
    val attributes: Map<String, String>?,
    val averagePrice: String?,
    val lowestPrice: String?,
    val highestPrice: String?,
    val lastSalePrice: String?,
    val saleCount: Int,
    val activeListingsCount: Int
)

data class GradePrice(
    val grade: String,
    val gradingCompany: String?,
    val averagePrice: String?,
    val lowestPrice: String?,
    val highestPrice: String?,
    val saleCount: Int
)

data class Sale(
    val id: Int,
    val source: String,
    val salePrice: String,
    val saleDate: String,
    val condition: String?,
    val grade: String?,
    val gradingCompany: String?,
    val platform: String?
)

data class PriceHistory(
    val date: String,
    val averagePrice: String,
    val saleCount: Int
)

data class TrendingItem(
    val id: Int,
    val name: String,
    val slug: String,
    val primaryImage: String?,
    val currentPrice: String?,
    val priceChange: String?,
    val priceChangePercent: String?,
    val trendDirection: TrendDirection
)

enum class TrendDirection {
    UP,
    DOWN,
    STABLE;

    companion object {
        fun fromString(value: String): TrendDirection {
            return when (value.lowercase()) {
                "up" -> UP
                "down" -> DOWN
                else -> STABLE
            }
        }
    }
}
