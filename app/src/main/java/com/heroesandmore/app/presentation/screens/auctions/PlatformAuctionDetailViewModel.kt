package com.heroesandmore.app.presentation.screens.auctions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.AuctionEvent
import com.heroesandmore.app.domain.model.AuctionLotSubmission
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

data class PlatformAuctionDetailUiState(
    val event: AuctionEvent? = null,
    val lots: List<Listing> = emptyList(),
    val mySubmissions: List<AuctionLotSubmission> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PlatformAuctionDetailViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val slug: String = savedStateHandle.get<String>("slug") ?: ""

    private val _uiState = MutableStateFlow(PlatformAuctionDetailUiState())
    val uiState: StateFlow<PlatformAuctionDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun refresh() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load event detail
            launch {
                when (val result = marketplaceRepository.getPlatformAuctionDetail(slug)) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(event = result.data) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                    }
                    is Resource.Loading -> {}
                }
            }

            // Load lots
            launch {
                when (val result = marketplaceRepository.getPlatformAuctionLots(slug)) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(lots = result.data ?: emptyList()) }
                    }
                    is Resource.Error -> {
                        // Don't overwrite event error
                    }
                    is Resource.Loading -> {}
                }
            }

            // Load my submissions for this event
            launch {
                when (val result = marketplaceRepository.getMySubmissions()) {
                    is Resource.Success -> {
                        val eventSubmissions = result.data?.filter { it.eventSlug == slug } ?: emptyList()
                        _uiState.update { it.copy(mySubmissions = eventSubmissions) }
                    }
                    is Resource.Error -> {
                        // Non-critical, user might not be logged in
                    }
                    is Resource.Loading -> {}
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
