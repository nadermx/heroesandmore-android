package com.heroesandmore.app.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.Listing
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val results: List<Listing> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
    val hasMore: Boolean = false,
    val currentPage: Int = 1,
    val currentQuery: String = ""
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(query: String) {
        if (query.isEmpty()) return
        
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null,
            currentQuery = query,
            currentPage = 1
        )

        viewModelScope.launch {
            when (val result = marketplaceRepository.getListings(
                page = 1,
                search = query
            )) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        results = result.data ?: emptyList(),
                        isLoading = false,
                        hasSearched = true,
                        hasMore = (result.data?.size ?: 0) >= 20
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message,
                        hasSearched = true
                    )
                }
                is Resource.Loading -> {
                    // Already loading
                }
            }
        }
    }

    fun loadMore() {
        if (_uiState.value.isLoading || !_uiState.value.hasMore) return

        val nextPage = _uiState.value.currentPage + 1
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = marketplaceRepository.getListings(
                page = nextPage,
                search = _uiState.value.currentQuery
            )) {
                is Resource.Success -> {
                    val newResults = result.data ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        results = _uiState.value.results + newResults,
                        isLoading = false,
                        currentPage = nextPage,
                        hasMore = newResults.size >= 20
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
