package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.CollectionsApi
import com.heroesandmore.app.data.dto.collections.*
import com.heroesandmore.app.domain.model.Collection
import com.heroesandmore.app.domain.model.CollectionDetail
import com.heroesandmore.app.domain.model.CollectionItem
import com.heroesandmore.app.domain.model.CollectionValue
import com.heroesandmore.app.domain.model.CollectionValueHistory
import com.heroesandmore.app.domain.repository.CollectionRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val collectionsApi: CollectionsApi
) : CollectionRepository {

    override suspend fun getCollections(): Resource<List<Collection>> {
        val result = safeApiCall { collectionsApi.getCollections() }
        return result.map { collections -> collections.map { it.toCollection() } }
    }

    override suspend fun createCollection(name: String, description: String?, isPublic: Boolean): Resource<Collection> {
        val result = safeApiCall { collectionsApi.createCollection(CreateCollectionRequest(name, description, isPublic)) }
        return result.map { it.toCollection() }
    }

    override suspend fun getCollectionDetail(id: Int): Resource<CollectionDetail> {
        val result = safeApiCall { collectionsApi.getCollectionDetail(id) }
        return result.map { it.toCollectionDetail() }
    }

    override suspend fun updateCollection(id: Int, name: String?, description: String?, isPublic: Boolean?): Resource<Collection> {
        val result = safeApiCall { collectionsApi.updateCollection(id, UpdateCollectionRequest(name, description, isPublic)) }
        return result.map { it.toCollection() }
    }

    override suspend fun deleteCollection(id: Int): Resource<Boolean> {
        val result = safeApiCall { collectionsApi.deleteCollection(id) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete collection")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun getCollectionItems(id: Int, page: Int): Resource<List<CollectionItem>> {
        val result = safeApiCall { collectionsApi.getCollectionItems(id, page) }
        return result.map { response -> response.results.map { it.toCollectionItem() } }
    }

    override suspend fun addCollectionItem(collectionId: Int, request: AddCollectionItemRequest): Resource<CollectionItem> {
        val result = safeApiCall { collectionsApi.addCollectionItem(collectionId, request) }
        return result.map { it.toCollectionItem() }
    }

    override suspend fun updateCollectionItem(collectionId: Int, itemId: Int, request: UpdateCollectionItemRequest): Resource<CollectionItem> {
        val result = safeApiCall { collectionsApi.updateCollectionItem(collectionId, itemId, request) }
        return result.map { it.toCollectionItem() }
    }

    override suspend fun removeCollectionItem(collectionId: Int, itemId: Int): Resource<Boolean> {
        val result = safeApiCall { collectionsApi.removeCollectionItem(collectionId, itemId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to remove item")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun getCollectionValue(id: Int): Resource<CollectionValue> {
        val result = safeApiCall { collectionsApi.getCollectionValue(id) }
        return result.map { it.toCollectionValue() }
    }

    override suspend fun getCollectionValueHistory(id: Int): Resource<List<CollectionValueHistory>> {
        val result = safeApiCall { collectionsApi.getCollectionValueHistory(id) }
        return result.map { history -> history.map { it.toCollectionValueHistory() } }
    }

    override suspend fun exportCollection(id: Int): Resource<String> {
        val result = safeApiCall { collectionsApi.exportCollection(id) }
        return when (result) {
            is Resource.Success -> Resource.success(result.data?.string() ?: "")
            is Resource.Error -> Resource.error(result.message ?: "Failed to export collection")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun importCollection(file: File): Resource<ImportResultDto> {
        val requestBody = file.asRequestBody("text/csv".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return safeApiCall { collectionsApi.importCollection(part) }
    }

    override suspend fun getPublicCollections(page: Int): Resource<List<Collection>> {
        val result = safeApiCall { collectionsApi.getPublicCollections(page) }
        return result.map { response -> response.results.map { it.toCollection() } }
    }

    // Mapping functions
    private fun CollectionDto.toCollection(): Collection = Collection(
        id = id,
        name = name,
        description = description,
        isPublic = isPublic,
        ownerUsername = ownerUsername,
        itemCount = itemCount,
        totalValue = totalValue,
        totalCost = totalCost,
        gainLoss = gainLoss,
        created = created
    )

    private fun CollectionDetailDto.toCollectionDetail(): CollectionDetail = CollectionDetail(
        id = id,
        name = name,
        description = description,
        isPublic = isPublic,
        ownerUsername = ownerUsername,
        itemCount = itemCount,
        totalValue = totalValue,
        totalCost = totalCost,
        gainLoss = gainLoss,
        items = items?.map { it.toCollectionItem() } ?: emptyList(),
        created = created
    )

    private fun CollectionItemDto.toCollectionItem(): CollectionItem = CollectionItem(
        id = id,
        itemName = itemName,
        condition = condition,
        grade = grade,
        gradingCompany = gradingCompany,
        purchasePrice = purchasePrice,
        purchaseDate = purchaseDate,
        currentValue = currentValue,
        imageUrl = imageUrl,
        notes = notes,
        created = created
    )

    private fun CollectionValueDto.toCollectionValue(): CollectionValue = CollectionValue(
        totalValue = totalValue,
        totalCost = totalCost,
        gainLoss = gainLoss,
        gainLossPercent = gainLossPercent,
        itemCount = itemCount
    )

    private fun CollectionValueHistoryDto.toCollectionValueHistory(): CollectionValueHistory = CollectionValueHistory(
        date = date,
        totalValue = totalValue,
        itemCount = itemCount
    )
}
