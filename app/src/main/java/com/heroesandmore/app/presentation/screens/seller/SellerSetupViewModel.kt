package com.heroesandmore.app.presentation.screens.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.User
import com.heroesandmore.app.domain.repository.AccountRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerSetupUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val launchUrl: String? = null
)

@HiltViewModel
class SellerSetupViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerSetupUiState())
    val uiState: StateFlow<SellerSetupUiState> = _uiState.asStateFlow()

    companion object {
        const val SELLER_SETUP_URL = "https://heroesandmore.com/marketplace/seller-setup/"
    }

    init {
        loadUser()
    }

    fun launchSellerSetup() {
        _uiState.update { it.copy(launchUrl = SELLER_SETUP_URL) }
    }

    fun onUrlLaunched() {
        _uiState.update { it.copy(launchUrl = null) }
    }

    fun refresh() {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = accountRepository.getCurrentUser()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            user = result.data,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message ?: "Failed to load user info",
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}
