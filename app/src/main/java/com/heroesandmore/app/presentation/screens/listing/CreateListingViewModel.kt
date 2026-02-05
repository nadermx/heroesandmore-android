package com.heroesandmore.app.presentation.screens.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.data.dto.marketplace.CreateListingRequest
import com.heroesandmore.app.domain.model.CategoryListItem
import com.heroesandmore.app.domain.repository.ItemsRepository
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateListingUiState(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val condition: String = "",
    val listingType: String = "fixed",
    val allowOffers: Boolean = true,
    val shippingPrice: String = "",
    val selectedCategory: CategoryListItem? = null,
    val selectedCategoryName: String? = null,
    val categories: List<CategoryListItem> = emptyList(),
    val categoryExpanded: Boolean = false,
    val conditionExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val titleError: String? = null,
    val priceError: String? = null,
    val createdListingId: Int? = null
) {
    val isValid: Boolean
        get() = title.isNotEmpty() && 
                price.isNotEmpty() && 
                condition.isNotEmpty() && 
                selectedCategory != null
}

@HiltViewModel
class CreateListingViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateListingUiState())
    val uiState: StateFlow<CreateListingUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            when (val result = itemsRepository.getCategoryList()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        categories = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
                is Resource.Loading -> {
                    // Loading
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = if (title.isEmpty()) "Title is required" else null
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(
            price = price,
            priceError = if (price.isEmpty()) "Price is required" else null
        )
    }

    fun toggleCategoryDropdown() {
        _uiState.value = _uiState.value.copy(
            categoryExpanded = !_uiState.value.categoryExpanded
        )
    }

    fun selectCategory(category: CategoryListItem) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            selectedCategoryName = category.name,
            categoryExpanded = false
        )
    }

    fun toggleConditionDropdown() {
        _uiState.value = _uiState.value.copy(
            conditionExpanded = !_uiState.value.conditionExpanded
        )
    }

    fun selectCondition(condition: String) {
        _uiState.value = _uiState.value.copy(
            condition = condition,
            conditionExpanded = false
        )
    }

    fun setListingType(type: String) {
        _uiState.value = _uiState.value.copy(listingType = type)
    }

    fun setAllowOffers(allow: Boolean) {
        _uiState.value = _uiState.value.copy(allowOffers = allow)
    }

    fun updateShippingPrice(price: String) {
        _uiState.value = _uiState.value.copy(shippingPrice = price)
    }

    fun saveDraft() {
        // Save as draft functionality
    }

    fun createListing() {
        val state = _uiState.value
        if (!state.isValid) return

        _uiState.value = state.copy(isLoading = true, error = null)

        val request = CreateListingRequest(
            title = state.title,
            description = state.description.ifEmpty { null },
            price = state.price,
            listingType = state.listingType,
            category = state.selectedCategory!!.id,
            condition = state.condition,
            allowOffers = state.allowOffers,
            shippingPrice = state.shippingPrice.ifEmpty { null }
        )

        viewModelScope.launch {
            when (val result = marketplaceRepository.createListing(request)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        createdListingId = result.data?.id
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {
                    // Already loading
                }
            }
        }
    }
}
