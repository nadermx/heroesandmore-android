package com.heroesandmore.app.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.heroesandmore.app.data.dto.collections.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CollectionsApiTest {

    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder().create()
    }

    // MARK: - Collection DTO Tests

    @Test
    fun `test collection dto deserialization`() {
        val json = """
        {
            "id": 1,
            "name": "My Baseball Cards",
            "description": "Collection of vintage baseball cards",
            "is_public": true,
            "cover_image": "https://example.com/cover.jpg",
            "item_count": 25,
            "total_value": "5000.00",
            "created": "2026-01-01T00:00:00Z",
            "updated": "2026-01-15T12:00:00Z"
        }
        """.trimIndent()

        val collection = gson.fromJson(json, CollectionDto::class.java)

        assertEquals(1, collection.id)
        assertEquals("My Baseball Cards", collection.name)
        assertTrue(collection.isPublic)
        assertEquals(25, collection.itemCount)
        assertEquals("5000.00", collection.totalValue)
    }

    @Test
    fun `test collection detail dto deserialization`() {
        val json = """
        {
            "id": 1,
            "name": "Premium Collection",
            "description": "High-end cards",
            "is_public": true,
            "cover_image": null,
            "owner": {
                "username": "collector1",
                "avatar_url": null
            },
            "item_count": 10,
            "total_value": "10000.00",
            "items": [],
            "created": "2025-12-01T00:00:00Z",
            "updated": "2026-01-15T00:00:00Z"
        }
        """.trimIndent()

        val detail = gson.fromJson(json, CollectionDetailDto::class.java)

        assertEquals(1, detail.id)
        assertEquals("Premium Collection", detail.name)
        assertEquals("collector1", detail.owner.username)
        assertEquals("10000.00", detail.totalValue)
    }

    // MARK: - Collection Item Tests

    @Test
    fun `test collection item dto deserialization`() {
        val json = """
        {
            "id": 1,
            "price_guide_item_id": 100,
            "price_guide_item_name": "Mickey Mantle 1952 Topps",
            "custom_name": null,
            "grade": "PSA 8",
            "grade_company": "PSA",
            "cert_number": "12345678",
            "purchase_price": "1500.00",
            "purchase_date": "2025-06-15",
            "current_value": "2500.00",
            "notes": "Great centering",
            "image_url": null
        }
        """.trimIndent()

        val item = gson.fromJson(json, CollectionItemDto::class.java)

        assertEquals(1, item.id)
        assertEquals("PSA 8", item.grade)
        assertEquals("PSA", item.gradeCompany)
        assertEquals("12345678", item.certNumber)
        assertEquals("1500.00", item.purchasePrice)
        assertEquals("2500.00", item.currentValue)
    }

    @Test
    fun `test collection item with custom name`() {
        val json = """
        {
            "id": 2,
            "price_guide_item_id": null,
            "price_guide_item_name": null,
            "custom_name": "My Custom Card",
            "grade": "Raw",
            "grade_company": null,
            "cert_number": null,
            "purchase_price": "50.00",
            "purchase_date": null,
            "current_value": null,
            "notes": null,
            "image_url": null
        }
        """.trimIndent()

        val item = gson.fromJson(json, CollectionItemDto::class.java)

        assertEquals(2, item.id)
        assertNull(item.priceGuideItemId)
        assertEquals("My Custom Card", item.customName)
        assertEquals("Raw", item.grade)
    }

    // MARK: - Value Tests

    @Test
    fun `test collection value dto deserialization`() {
        val json = """
        {
            "total_value": "5000.00",
            "total_cost": "3500.00",
            "gain_loss": "1500.00",
            "item_count": 25
        }
        """.trimIndent()

        val value = gson.fromJson(json, CollectionValueDto::class.java)

        assertEquals("5000.00", value.totalValue)
        assertEquals("3500.00", value.totalCost)
        assertEquals("1500.00", value.gainLoss)
        assertEquals(25, value.itemCount)
    }

    @Test
    fun `test collection value snapshot dto deserialization`() {
        val json = """
        {
            "date": "2026-01-15",
            "value": "5250.00"
        }
        """.trimIndent()

        val snapshot = gson.fromJson(json, CollectionValueSnapshotDto::class.java)

        assertEquals("2026-01-15", snapshot.date)
        assertEquals("5250.00", snapshot.value)
    }

    // MARK: - Import Result Tests

    @Test
    fun `test import result dto deserialization`() {
        val json = """
        {
            "collection_id": 5,
            "collection_name": "Imported Collection",
            "items_imported": 15,
            "items_total": 20
        }
        """.trimIndent()

        val result = gson.fromJson(json, ImportResultDto::class.java)

        assertEquals(5, result.collectionId)
        assertEquals("Imported Collection", result.collectionName)
        assertEquals(15, result.itemsImported)
        assertEquals(20, result.itemsTotal)
    }

    // MARK: - Request Serialization Tests

    @Test
    fun `test create collection request serialization`() {
        val request = CreateCollectionRequest(
            name = "New Collection",
            description = "My new collection",
            isPublic = true
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("New Collection"))
        assertTrue(json.contains("is_public"))
    }

    @Test
    fun `test update collection request serialization`() {
        val request = UpdateCollectionRequest(
            name = "Updated Name",
            description = "Updated description",
            isPublic = false
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("Updated Name"))
        assertTrue(json.contains("is_public"))
    }

    @Test
    fun `test add collection item request serialization`() {
        val request = AddCollectionItemRequest(
            priceGuideItemId = 100,
            customName = null,
            grade = "PSA 9",
            gradeCompany = "PSA",
            certNumber = "98765432",
            purchasePrice = "500.00",
            purchaseDate = "2026-01-10",
            notes = "New addition"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("price_guide_item_id"))
        assertTrue(json.contains("PSA 9"))
        assertTrue(json.contains("98765432"))
    }

    @Test
    fun `test add collection item with custom name`() {
        val request = AddCollectionItemRequest(
            priceGuideItemId = null,
            customName = "Custom Item",
            grade = "Raw",
            gradeCompany = null,
            certNumber = null,
            purchasePrice = "25.00",
            purchaseDate = null,
            notes = null
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("custom_name"))
        assertTrue(json.contains("Custom Item"))
    }

    @Test
    fun `test update collection item request serialization`() {
        val request = UpdateCollectionItemRequest(
            grade = "PSA 10",
            currentValue = "1000.00",
            notes = "Upgraded grade"
        )
        val json = gson.toJson(request)

        assertTrue(json.contains("PSA 10"))
        assertTrue(json.contains("current_value"))
    }
}
