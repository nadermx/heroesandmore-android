package com.heroesandmore.app.presentation.screens.auctions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.AuctionEvent
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlatformAuctionsUiState(
    val events: List<AuctionEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PlatformAuctionsViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlatformAuctionsUiState())
    val uiState: StateFlow<PlatformAuctionsUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun refresh() {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = marketplaceRepository.getPlatformAuctionEvents()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            events = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Failed to load auction events",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}
