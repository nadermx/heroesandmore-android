package com.heroesandmore.app.data.dto.collections

import com.google.gson.annotations.SerializedName

data class CollectionDto(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("is_public")
    val isPublic: Boolean,
    @SerializedName("owner_username")
    val ownerUsername: String,
    @SerializedName("item_count")
    val itemCount: Int,
    @SerializedName("total_value")
    val totalValue: String?,
    @SerializedName("total_cost")
    val totalCost: String?,
    @SerializedName("gain_loss")
    val gainLoss: String?,
    val created: String
)

data class CollectionDetailDto(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("is_public")
    val isPublic: Boolean,
    @SerializedName("owner_username")
    val ownerUsername: String,
    @SerializedName("item_count")
    val itemCount: Int,
    @SerializedName("total_value")
    val totalValue: String?,
    @SerializedName("total_cost")
    val totalCost: String?,
    @SerializedName("gain_loss")
    val gainLoss: String?,
    val items: List<CollectionItemDto>?,
    val created: String
)

data class CollectionItemDto(
    val id: Int,
    @SerializedName("item_name")
    val itemName: String,
    val condition: String?,
    val grade: String?,
    @SerializedName("grading_company")
    val gradingCompany: String?,
    @SerializedName("purchase_price")
    val purchasePrice: String?,
    @SerializedName("purchase_date")
    val purchaseDate: String?,
    @SerializedName("current_value")
    val currentValue: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val notes: String?,
    val created: String
)

data class CreateCollectionRequest(
    val name: String,
    val description: String? = null,
    @SerializedName("is_public")
    val isPublic: Boolean = false
)

data class UpdateCollectionRequest(
    val name: String? = null,
    val description: String? = null,
    @SerializedName("is_public")
    val isPublic: Boolean? = null
)

data class AddCollectionItemRequest(
    val name: String,
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

data class UpdateCollectionItemRequest(
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

data class CollectionValueDto(
    @SerializedName("total_value")
    val totalValue: String,
    @SerializedName("total_cost")
    val totalCost: String,
    @SerializedName("gain_loss")
    val gainLoss: String,
    @SerializedName("gain_loss_percent")
    val gainLossPercent: String?,
    @SerializedName("item_count")
    val itemCount: Int
)

data class CollectionValueHistoryDto(
    val date: String,
    @SerializedName("total_value")
    val totalValue: String,
    @SerializedName("item_count")
    val itemCount: Int
)

data class ImportResultDto(
    @SerializedName("items_imported")
    val itemsImported: Int,
    @SerializedName("items_failed")
    val itemsFailed: Int,
    val errors: List<String>?
)
