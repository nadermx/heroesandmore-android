package com.heroesandmore.app.presentation.screens.auctions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class SubmitLotUiState(
    val myListings: List<Listing> = emptyList(),
    val selectedListingId: Int? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val submitSuccess: Boolean = false,
    val submission: AuctionLotSubmission? = null
)

@HiltViewModel
class SubmitLotViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val eventSlug: String = savedStateHandle.get<String>("slug") ?: ""

    private val _uiState = MutableStateFlow(SubmitLotUiState())
    val uiState: StateFlow<SubmitLotUiState> = _uiState.asStateFlow()

    init {
        loadMyListings()
    }

    fun selectListing(listingId: Int) {
        _uiState.update { it.copy(selectedListingId = listingId) }
    }

    fun submitLot() {
        val listingId = _uiState.value.selectedListingId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }

            when (val result = marketplaceRepository.submitAuctionLot(eventSlug, listingId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            submitSuccess = true,
                            submission = result.data
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            error = result.message ?: "Failed to submit lot"
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun loadMyListings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = marketplaceRepository.getMyActiveListings()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            myListings = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Failed to load your listings",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}
