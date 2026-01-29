package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.data.dto.collections.*
import com.heroesandmore.app.domain.model.Collection
import com.heroesandmore.app.domain.model.CollectionDetail
import com.heroesandmore.app.domain.model.CollectionItem
import com.heroesandmore.app.domain.model.CollectionValue
import com.heroesandmore.app.domain.model.CollectionValueHistory
import com.heroesandmore.app.util.Resource
import java.io.File

interface CollectionRepository {
    suspend fun getCollections(): Resource<List<Collection>>
    suspend fun createCollection(name: String, description: String?, isPublic: Boolean): Resource<Collection>
    suspend fun getCollectionDetail(id: Int): Resource<CollectionDetail>
    suspend fun updateCollection(id: Int, name: String?, description: String?, isPublic: Boolean?): Resource<Collection>
    suspend fun deleteCollection(id: Int): Resource<Boolean>
    suspend fun getCollectionItems(id: Int, page: Int = 1): Resource<List<CollectionItem>>
    suspend fun addCollectionItem(collectionId: Int, request: AddCollectionItemRequest): Resource<CollectionItem>
    suspend fun updateCollectionItem(collectionId: Int, itemId: Int, request: UpdateCollectionItemRequest): Resource<CollectionItem>
    suspend fun removeCollectionItem(collectionId: Int, itemId: Int): Resource<Boolean>
    suspend fun getCollectionValue(id: Int): Resource<CollectionValue>
    suspend fun getCollectionValueHistory(id: Int): Resource<List<CollectionValueHistory>>
    suspend fun exportCollection(id: Int): Resource<String>
    suspend fun importCollection(file: File): Resource<ImportResultDto>
    suspend fun getPublicCollections(page: Int = 1): Resource<List<Collection>>
}
