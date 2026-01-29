package com.heroesandmore.app.domain.model

data class Collection(
    val id: Int,
    val name: String,
    val description: String?,
    val isPublic: Boolean,
    val ownerUsername: String,
    val itemCount: Int,
    val totalValue: String?,
    val totalCost: String?,
    val gainLoss: String?,
    val created: String
)

data class CollectionDetail(
    val id: Int,
    val name: String,
    val description: String?,
    val isPublic: Boolean,
    val ownerUsername: String,
    val itemCount: Int,
    val totalValue: String?,
    val totalCost: String?,
    val gainLoss: String?,
    val items: List<CollectionItem>,
    val created: String
)

data class CollectionItem(
    val id: Int,
    val itemName: String,
    val condition: String?,
    val grade: String?,
    val gradingCompany: String?,
    val purchasePrice: String?,
    val purchaseDate: String?,
    val currentValue: String?,
    val imageUrl: String?,
    val notes: String?,
    val created: String
)

data class CollectionValue(
    val totalValue: String,
    val totalCost: String,
    val gainLoss: String,
    val gainLossPercent: String?,
    val itemCount: Int
)

data class CollectionValueHistory(
    val date: String,
    val totalValue: String,
    val itemCount: Int
)
