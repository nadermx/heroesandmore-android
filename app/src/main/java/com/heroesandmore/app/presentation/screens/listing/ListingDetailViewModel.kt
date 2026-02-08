package com.heroesandmore.app.presentation.screens.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.ListingDetail
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListingDetailUiState(
    val listing: ListingDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBidDialog: Boolean = false,
    val showOfferDialog: Boolean = false,
    val actionError: String? = null,
    val actionSuccess: String? = null,
    val selectedQuantity: Int = 1
)

@HiltViewModel
class ListingDetailViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListingDetailUiState())
    val uiState: StateFlow<ListingDetailUiState> = _uiState.asStateFlow()

    private var currentListingId: Int? = null

    fun loadListing(listingId: Int) {
        if (listingId == currentListingId && _uiState.value.listing != null) return
        currentListingId = listingId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = marketplaceRepository.getListingDetail(listingId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            listing = result.data,
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

    fun toggleSaved() {
        val listing = _uiState.value.listing ?: return

        viewModelScope.launch {
            val result = if (listing.isSaved) {
                marketplaceRepository.unsaveListing(listing.id)
            } else {
                marketplaceRepository.saveListing(listing.id)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            listing = it.listing?.copy(isSaved = !listing.isSaved)
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(actionError = result.message)
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun showBidDialog() {
        _uiState.update { it.copy(showBidDialog = true) }
    }

    fun hideBidDialog() {
        _uiState.update { it.copy(showBidDialog = false) }
    }

    fun showOfferDialog() {
        _uiState.update { it.copy(showOfferDialog = true) }
    }

    fun hideOfferDialog() {
        _uiState.update { it.copy(showOfferDialog = false) }
    }

    fun placeBid(amount: String) {
        val listingId = currentListingId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(showBidDialog = false) }

            when (val result = marketplaceRepository.placeBid(listingId, amount)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(actionSuccess = "Bid placed successfully!")
                    }
                    // Reload listing to get updated price
                    loadListing(listingId)
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(actionError = result.message)
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun makeOffer(amount: String, message: String?) {
        val listingId = currentListingId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(showOfferDialog = false) }

            when (val result = marketplaceRepository.makeOffer(listingId, amount, message)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(actionSuccess = "Offer sent successfully!")
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(actionError = result.message)
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun updateQuantity(quantity: Int) {
        val maxAvailable = _uiState.value.listing?.quantityAvailable ?: 1
        _uiState.update { it.copy(selectedQuantity = quantity.coerceIn(1, maxAvailable)) }
    }

    fun clearActionMessages() {
        _uiState.update { it.copy(actionError = null, actionSuccess = null) }
    }
}
