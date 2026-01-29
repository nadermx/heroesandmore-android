package com.heroesandmore.app.data.dto.scanner

import com.google.gson.annotations.SerializedName

data class ScanResultDto(
    val id: Int,
    val status: String,
    @SerializedName("scanned_image")
    val scannedImage: String,
    @SerializedName("identified_item")
    val identifiedItem: IdentifiedItemDto?,
    @SerializedName("confidence_score")
    val confidenceScore: Double?,
    @SerializedName("alternative_matches")
    val alternativeMatches: List<AlternativeMatchDto>?,
    @SerializedName("processing_time")
    val processingTime: Double?,
    val created: String
)

data class IdentifiedItemDto(
    val id: Int,
    val name: String,
    val category: String?,
    @SerializedName("category_id")
    val categoryId: Int?,
    val year: Int?,
    val set: String?,
    @SerializedName("card_number")
    val cardNumber: String?,
    @SerializedName("estimated_value")
    val estimatedValue: String?,
    @SerializedName("price_range_low")
    val priceRangeLow: String?,
    @SerializedName("price_range_high")
    val priceRangeHigh: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("price_guide_id")
    val priceGuideId: Int?
)

data class AlternativeMatchDto(
    val id: Int,
    val name: String,
    @SerializedName("confidence_score")
    val confidenceScore: Double,
    @SerializedName("estimated_value")
    val estimatedValue: String?,
    @SerializedName("image_url")
    val imageUrl: String?
)

data class CreateFromScanRequest(
    val title: String? = null,
    val description: String? = null,
    val price: String,
    @SerializedName("listing_type")
    val listingType: String = "fixed",
    val category: Int? = null,
    val condition: String,
    @SerializedName("grading_company")
    val gradingCompany: String? = null,
    val grade: String? = null,
    @SerializedName("allow_offers")
    val allowOffers: Boolean = true,
    @SerializedName("shipping_price")
    val shippingPrice: String? = null
)

data class AddToCollectionFromScanRequest(
    @SerializedName("collection_id")
    val collectionId: Int,
    val name: String? = null,
    val condition: String? = null,
    val grade: String? = null,
    @SerializedName("grading_company")
    val gradingCompany: String? = null,
    @SerializedName("purchase_price")
    val purchasePrice: String? = null,
    @SerializedName("purchase_date")
    val purchaseDate: String? = null,
    val notes: String? = null
)

data class ScanSessionDto(
    val id: Int,
    val name: String?,
    @SerializedName("scan_count")
    val scanCount: Int,
    val status: String,
    @SerializedName("destination_type")
    val destinationType: String?,
    @SerializedName("destination_id")
    val destinationId: Int?,
    val created: String
)

data class CreateScanSessionRequest(
    val name: String? = null,
    @SerializedName("destination_type")
    val destinationType: String? = null,
    @SerializedName("destination_id")
    val destinationId: Int? = null
)
