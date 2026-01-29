package com.heroesandmore.app.presentation.screens.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.Category
import com.heroesandmore.app.domain.model.Listing
import com.heroesandmore.app.domain.repository.ItemsRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowseUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = itemsRepository.getCategoryTree()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}

data class CategoryListingsUiState(
    val categoryName: String? = null,
    val listings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGridView: Boolean = true,
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)

@HiltViewModel
class CategoryListingsViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryListingsUiState())
    val uiState: StateFlow<CategoryListingsUiState> = _uiState.asStateFlow()

    private var currentSlug: String? = null

    fun loadCategory(slug: String) {
        if (slug == currentSlug && _uiState.value.listings.isNotEmpty()) return
        currentSlug = slug

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load category details
            launch {
                when (val result = itemsRepository.getCategory(slug)) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(categoryName = result.data?.name) }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }

            // Load listings
            when (val result = itemsRepository.getCategoryListings(slug, 1)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            listings = result.data ?: emptyList(),
                            isLoading = false,
                            currentPage = 1,
                            hasMore = (result.data?.size ?: 0) >= 20
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun loadMore() {
        val slug = currentSlug ?: return
        if (_uiState.value.isLoading || !_uiState.value.hasMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val nextPage = _uiState.value.currentPage + 1
            when (val result = itemsRepository.getCategoryListings(slug, nextPage)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            listings = it.listings + (result.data ?: emptyList()),
                            isLoading = false,
                            currentPage = nextPage,
                            hasMore = (result.data?.size ?: 0) >= 20
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun toggleViewMode() {
        _uiState.update { it.copy(isGridView = !it.isGridView) }
    }
}
