package com.heroesandmore.app.domain.model

data class ScanResult(
    val id: Int,
    val status: ScanStatus,
    val scannedImage: String,
    val identifiedItem: IdentifiedItem?,
    val confidenceScore: Double?,
    val alternativeMatches: List<AlternativeMatch>,
    val processingTime: Double?,
    val created: String
)

enum class ScanStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED;

    companion object {
        fun fromString(value: String): ScanStatus {
            return when (value.lowercase()) {
                "processing" -> PROCESSING
                "completed" -> COMPLETED
                "failed" -> FAILED
                else -> PENDING
            }
        }
    }
}

data class IdentifiedItem(
    val id: Int,
    val name: String,
    val category: String?,
    val categoryId: Int?,
    val year: Int?,
    val set: String?,
    val cardNumber: String?,
    val estimatedValue: String?,
    val priceRangeLow: String?,
    val priceRangeHigh: String?,
    val imageUrl: String?,
    val priceGuideId: Int?
)

data class AlternativeMatch(
    val id: Int,
    val name: String,
    val confidenceScore: Double,
    val estimatedValue: String?,
    val imageUrl: String?
)

data class ScanSession(
    val id: Int,
    val name: String?,
    val scanCount: Int,
    val status: String,
    val destinationType: String?,
    val destinationId: Int?,
    val created: String
)
