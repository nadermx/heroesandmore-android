package com.heroesandmore.app.presentation.screens.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.Collection
import com.heroesandmore.app.domain.repository.AuthRepository
import com.heroesandmore.app.domain.repository.CollectionRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionsUiState(
    val collections: List<Collection> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val totalValue: String? = null,
    val totalItems: Int = 0,
    val totalGainLoss: String? = null
)

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionsUiState())
    val uiState: StateFlow<CollectionsUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.getAuthStateFlow().collect { isLoggedIn ->
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
                if (isLoggedIn) {
                    loadCollections()
                } else {
                    _uiState.update { it.copy(collections = emptyList()) }
                }
            }
        }
    }

    fun loadCollections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = collectionRepository.getCollections()) {
                is Resource.Success -> {
                    val collections = result.data ?: emptyList()

                    // Calculate totals
                    var totalValue = 0.0
                    var totalCost = 0.0
                    var totalItems = 0

                    collections.forEach { collection ->
                        collection.totalValue?.toDoubleOrNull()?.let { totalValue += it }
                        collection.totalCost?.toDoubleOrNull()?.let { totalCost += it }
                        totalItems += collection.itemCount
                    }

                    val gainLoss = if (totalCost > 0) totalValue - totalCost else null

                    _uiState.update {
                        it.copy(
                            collections = collections,
                            totalValue = if (totalValue > 0) "%.2f".format(totalValue) else null,
                            totalItems = totalItems,
                            totalGainLoss = gainLoss?.let { gl -> "%.2f".format(gl) },
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

    fun createCollection(name: String, description: String?, isPublic: Boolean) {
        viewModelScope.launch {
            when (val result = collectionRepository.createCollection(name, description, isPublic)) {
                is Resource.Success -> {
                    loadCollections()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun deleteCollection(id: Int) {
        viewModelScope.launch {
            when (val result = collectionRepository.deleteCollection(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(collections = it.collections.filter { c -> c.id != id })
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }
}
