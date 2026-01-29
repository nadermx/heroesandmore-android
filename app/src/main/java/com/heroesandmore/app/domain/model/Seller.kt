package com.heroesandmore.app.domain.model

data class DashboardStats(
    val totalSales: String,
    val totalOrders: Int,
    val pendingOrders: Int,
    val activeListings: Int,
    val totalViews: Int,
    val conversionRate: Double?,
    val averageSalePrice: String?,
    val thisMonthSales: String?,
    val lastMonthSales: String?,
    val salesChangePercent: Double?
)

data class Analytics(
    val salesByDay: List<SalesByDay>,
    val salesByCategory: List<SalesByCategory>,
    val topItems: List<TopItem>,
    val trafficSources: List<TrafficSource>?
)

data class SalesByDay(
    val date: String,
    val sales: String,
    val orders: Int
)

data class SalesByCategory(
    val category: String,
    val sales: String,
    val count: Int,
    val percentage: Double
)

data class TopItem(
    val id: Int,
    val title: String,
    val image: String?,
    val sales: String,
    val quantity: Int
)

data class TrafficSource(
    val source: String,
    val visits: Int,
    val conversions: Int,
    val conversionRate: Double
)

data class Subscription(
    val id: Int,
    val plan: String,
    val planName: String,
    val status: SubscriptionStatus,
    val currentPeriodStart: String,
    val currentPeriodEnd: String,
    val cancelAtPeriodEnd: Boolean,
    val features: List<String>?
)

enum class SubscriptionStatus {
    ACTIVE,
    CANCELLED,
    PAST_DUE,
    TRIALING,
    NONE;

    companion object {
        fun fromString(value: String): SubscriptionStatus {
            return when (value.lowercase()) {
                "active" -> ACTIVE
                "cancelled" -> CANCELLED
                "past_due" -> PAST_DUE
                "trialing" -> TRIALING
                else -> NONE
            }
        }
    }
}

data class InventoryItem(
    val id: Int,
    val name: String,
    val description: String?,
    val image: String?,
    val categoryId: Int?,
    val categoryName: String?,
    val quantity: Int,
    val costPrice: String?,
    val targetPrice: String?,
    val condition: String?,
    val grade: String?,
    val gradingCompany: String?,
    val location: String?,
    val sku: String?,
    val isListed: Boolean,
    val listingId: Int?,
    val created: String
)

data class BulkImport(
    val id: Int,
    val filename: String,
    val status: ImportStatus,
    val totalRows: Int,
    val processedRows: Int,
    val successCount: Int,
    val errorCount: Int,
    val autoPublish: Boolean,
    val defaultCategory: Int?,
    val created: String,
    val completed: String?
)

enum class ImportStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED;

    companion object {
        fun fromString(value: String): ImportStatus {
            return when (value.lowercase()) {
                "processing" -> PROCESSING
                "completed" -> COMPLETED
                "failed" -> FAILED
                else -> PENDING
            }
        }
    }
}

data class BulkImportRow(
    val id: Int,
    val rowNumber: Int,
    val status: String,
    val rawData: Map<String, String>?,
    val listingId: Int?,
    val errorMessage: String?
)
