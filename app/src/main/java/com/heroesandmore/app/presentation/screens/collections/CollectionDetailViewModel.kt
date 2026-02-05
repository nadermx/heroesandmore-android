package com.heroesandmore.app.presentation.screens.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroesandmore.app.domain.model.CollectionDetail
import com.heroesandmore.app.domain.model.CollectionItem
import com.heroesandmore.app.domain.repository.CollectionRepository
import com.heroesandmore.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionDetailUiState(
    val collection: CollectionDetail? = null,
    val items: List<CollectionItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false,
    val showAddItemDialog: Boolean = false
)

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionDetailUiState())
    val uiState: StateFlow<CollectionDetailUiState> = _uiState.asStateFlow()

    fun loadCollection(collectionId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            when (val result = collectionRepository.getCollectionDetail(collectionId)) {
                is Resource.Success -> {
                    result.data?.let { detail ->
                        _uiState.value = _uiState.value.copy(
                            collection = detail,
                            items = detail.items,
                            isLoading = false
                        )
                    }
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

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(isEditMode = !_uiState.value.isEditMode)
    }

    fun showAddItemDialog() {
        _uiState.value = _uiState.value.copy(showAddItemDialog = true)
    }

    fun hideAddItemDialog() {
        _uiState.value = _uiState.value.copy(showAddItemDialog = false)
    }

    fun deleteItem(itemId: Int) {
        val collectionId = _uiState.value.collection?.id ?: return

        viewModelScope.launch {
            when (val result = collectionRepository.removeCollectionItem(collectionId, itemId)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        items = _uiState.value.items.filter { it.id != itemId }
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(error = result.message)
                }
                is Resource.Loading -> {
                    // Loading
                }
            }
        }
    }
}
