package com.heroesandmore.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.Offer
import com.heroesandmore.app.domain.repository.MarketplaceRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyOffersUiState(
    val offers: List<Offer> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val actionInProgress: Int? = null, // offerId of action in progress
    val actionSuccess: String? = null
)

@HiltViewModel
class MyOffersViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyOffersUiState())
    val uiState: StateFlow<MyOffersUiState> = _uiState.asStateFlow()

    init {
        loadOffers()
    }

    fun loadOffers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = marketplaceRepository.getOffers()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            offers = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Failed to load offers",
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun acceptOffer(offerId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInProgress = offerId) }

            when (val result = marketplaceRepository.acceptOffer(offerId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(actionInProgress = null, actionSuccess = "Offer accepted") }
                    loadOffers()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            actionInProgress = null,
                            error = result.message ?: "Failed to accept offer"
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun declineOffer(offerId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInProgress = offerId) }

            when (val result = marketplaceRepository.declineOffer(offerId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(actionInProgress = null, actionSuccess = "Offer declined") }
                    loadOffers()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            actionInProgress = null,
                            error = result.message ?: "Failed to decline offer"
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun counterOffer(offerId: Int, amount: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInProgress = offerId) }

            when (val result = marketplaceRepository.counterOffer(offerId, amount)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(actionInProgress = null, actionSuccess = "Counter-offer sent") }
                    loadOffers()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            actionInProgress = null,
                            error = result.message ?: "Failed to send counter-offer"
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun acceptCounterOffer(offerId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInProgress = offerId) }

            when (val result = marketplaceRepository.acceptCounterOffer(offerId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            actionInProgress = null,
                            actionSuccess = "Counter-offer accepted! Proceed to checkout."
                        )
                    }
                    loadOffers()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            actionInProgress = null,
                            error = result.message ?: "Failed to accept counter-offer"
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun declineCounterOffer(offerId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInProgress = offerId) }

            when (val result = marketplaceRepository.declineCounterOffer(offerId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(actionInProgress = null, actionSuccess = "Counter-offer declined") }
                    loadOffers()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            actionInProgress = null,
                            error = result.message ?: "Failed to decline counter-offer"
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(actionSuccess = null) }
    }
}
