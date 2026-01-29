package com.heroesandmore.app.data.dto.seller

import com.google.gson.annotations.SerializedName

data class DashboardStatsDto(
    @SerializedName("total_sales")
    val totalSales: String,
    @SerializedName("total_orders")
    val totalOrders: Int,
    @SerializedName("pending_orders")
    val pendingOrders: Int,
    @SerializedName("active_listings")
    val activeListings: Int,
    @SerializedName("total_views")
    val totalViews: Int,
    @SerializedName("conversion_rate")
    val conversionRate: Double?,
    @SerializedName("average_sale_price")
    val averageSalePrice: String?,
    @SerializedName("this_month_sales")
    val thisMonthSales: String?,
    @SerializedName("last_month_sales")
    val lastMonthSales: String?,
    @SerializedName("sales_change_percent")
    val salesChangePercent: Double?
)

data class AnalyticsDto(
    @SerializedName("sales_by_day")
    val salesByDay: List<SalesByDayDto>,
    @SerializedName("sales_by_category")
    val salesByCategory: List<SalesByCategoryDto>,
    @SerializedName("top_items")
    val topItems: List<TopItemDto>,
    @SerializedName("traffic_sources")
    val trafficSources: List<TrafficSourceDto>?
)

data class SalesByDayDto(
    val date: String,
    val sales: String,
    val orders: Int
)

data class SalesByCategoryDto(
    val category: String,
    val sales: String,
    val count: Int,
    val percentage: Double
)

data class TopItemDto(
    val id: Int,
    val title: String,
    val image: String?,
    val sales: String,
    val quantity: Int
)

data class TrafficSourceDto(
    val source: String,
    val visits: Int,
    val conversions: Int,
    @SerializedName("conversion_rate")
    val conversionRate: Double
)

data class SubscriptionDto(
    val id: Int,
    val plan: String,
    @SerializedName("plan_name")
    val planName: String,
    val status: String,
    @SerializedName("current_period_start")
    val currentPeriodStart: String,
    @SerializedName("current_period_end")
    val currentPeriodEnd: String,
    @SerializedName("cancel_at_period_end")
    val cancelAtPeriodEnd: Boolean,
    val features: List<String>?
)

data class InventoryItemDto(
    val id: Int,
    val name: String,
    val description: String?,
    val image: String?,
    @SerializedName("category_id")
    val categoryId: Int?,
    @SerializedName("category_name")
    val categoryName: String?,
    val quantity: Int,
    @SerializedName("cost_price")
    val costPrice: String?,
    @SerializedName("target_price")
    val targetPrice: String?,
    val condition: String?,
    val grade: String?,
    @SerializedName("grading_company")
    val gradingCompany: String?,
    val location: String?,
    val sku: String?,
    @SerializedName("is_listed")
    val isListed: Boolean,
    @SerializedName("listing_id")
    val listingId: Int?,
    val created: String
)

data class CreateInventoryItemRequest(
    val name: String,
    val description: String? = null,
    @SerializedName("category_id")
    val categoryId: Int? = null,
    val quantity: Int = 1,
    @SerializedName("cost_price")
    val costPrice: String? = null,
    @SerializedName("target_price")
    val targetPrice: String? = null,
    val condition: String? = null,
    val grade: String? = null,
    @SerializedName("grading_company")
    val gradingCompany: String? = null,
    val location: String? = null,
    val sku: String? = null
)

data class UpdateInventoryItemRequest(
    val name: String? = null,
    val description: String? = null,
    @SerializedName("category_id")
    val categoryId: Int? = null,
    val quantity: Int? = null,
    @SerializedName("cost_price")
    val costPrice: String? = null,
    @SerializedName("target_price")
    val targetPrice: String? = null,
    val condition: String? = null,
    val grade: String? = null,
    @SerializedName("grading_company")
    val gradingCompany: String? = null,
    val location: String? = null,
    val sku: String? = null
)

data class BulkImportDto(
    val id: Int,
    val filename: String,
    val status: String,
    @SerializedName("total_rows")
    val totalRows: Int,
    @SerializedName("processed_rows")
    val processedRows: Int,
    @SerializedName("success_count")
    val successCount: Int,
    @SerializedName("error_count")
    val errorCount: Int,
    @SerializedName("auto_publish")
    val autoPublish: Boolean,
    @SerializedName("default_category")
    val defaultCategory: Int?,
    val created: String,
    val completed: String?
)

data class BulkImportRowDto(
    val id: Int,
    @SerializedName("row_number")
    val rowNumber: Int,
    val status: String,
    @SerializedName("raw_data")
    val rawData: Map<String, String>?,
    @SerializedName("listing_id")
    val listingId: Int?,
    @SerializedName("error_message")
    val errorMessage: String?
)
