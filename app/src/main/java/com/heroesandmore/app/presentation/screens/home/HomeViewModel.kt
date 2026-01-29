package com.heroesandmore.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.Listing
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val featuredListings: List<Listing> = emptyList(),
    val endingSoon: List<Listing> = emptyList(),
    val recentListings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun refresh() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load featured listings
            launch {
                when (val result = marketplaceRepository.getListings(sort = "featured")) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(featuredListings = result.data ?: emptyList()) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }
                    is Resource.Loading -> {}
                }
            }

            // Load ending soon auctions
            launch {
                when (val result = marketplaceRepository.getEndingSoon()) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(endingSoon = result.data ?: emptyList()) }
                    }
                    is Resource.Error -> {
                        // Don't show error for this section
                    }
                    is Resource.Loading -> {}
                }
            }

            // Load recent listings
            launch {
                when (val result = marketplaceRepository.getListings(sort = "-created")) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(recentListings = result.data ?: emptyList()) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }
                    is Resource.Loading -> {}
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
