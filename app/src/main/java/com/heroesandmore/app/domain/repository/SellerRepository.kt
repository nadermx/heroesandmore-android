package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.data.dto.seller.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource
import java.io.File

interface SellerRepository {
    suspend fun getDashboard(): Resource<DashboardStats>
    suspend fun getAnalytics(days: Int = 30): Resource<Analytics>
    suspend fun getSubscription(): Resource<Subscription>

    // Inventory
    suspend fun getInventory(page: Int = 1, search: String? = null, ordering: String? = null): Resource<List<InventoryItem>>
    suspend fun addInventoryItem(request: CreateInventoryItemRequest): Resource<InventoryItem>
    suspend fun updateInventoryItem(id: Int, request: UpdateInventoryItemRequest): Resource<InventoryItem>
    suspend fun deleteInventoryItem(id: Int): Resource<Boolean>
    suspend fun createListingFromInventory(id: Int): Resource<ListingDetail>

    // Bulk Imports
    suspend fun getBulkImports(page: Int = 1): Resource<List<BulkImport>>
    suspend fun createBulkImport(file: File, autoPublish: Boolean, defaultCategory: Int?): Resource<BulkImport>
    suspend fun getBulkImport(id: Int): Resource<BulkImport>
    suspend fun getBulkImportRows(id: Int): Resource<List<BulkImportRow>>

    // Orders
    suspend fun getSellerOrders(page: Int = 1): Resource<List<Order>>
    suspend fun getSalesHistory(page: Int = 1): Resource<List<Order>>
}
