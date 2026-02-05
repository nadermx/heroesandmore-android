package com.heroesandmore.app.presentation.screens.listing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingScreen(
    onNavigateBack: () -> Unit,
    onListingCreated: (Int) -> Unit,
    viewModel: CreateListingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.createdListingId) {
        uiState.createdListingId?.let { id ->
            onListingCreated(id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Listing") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.saveDraft() },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Save Draft")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            item {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.titleError != null,
                    supportingText = uiState.titleError?.let { { Text(it) } }
                )
            }

            // Description
            item {
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }

            // Price
            item {
                OutlinedTextField(
                    value = uiState.price,
                    onValueChange = { viewModel.updatePrice(it) },
                    label = { Text("Price *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("$") },
                    isError = uiState.priceError != null,
                    supportingText = uiState.priceError?.let { { Text(it) } }
                )
            }

            // Category
            item {
                ExposedDropdownMenuBox(
                    expanded = uiState.categoryExpanded,
                    onExpandedChange = { viewModel.toggleCategoryDropdown() }
                ) {
                    OutlinedTextField(
                        value = uiState.selectedCategoryName ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = uiState.categoryExpanded,
                        onDismissRequest = { viewModel.toggleCategoryDropdown() }
                    ) {
                        uiState.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = { viewModel.selectCategory(category) }
                            )
                        }
                    }
                }
            }

            // Condition
            item {
                ExposedDropdownMenuBox(
                    expanded = uiState.conditionExpanded,
                    onExpandedChange = { viewModel.toggleConditionDropdown() }
                ) {
                    OutlinedTextField(
                        value = uiState.condition,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Condition *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.conditionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = uiState.conditionExpanded,
                        onDismissRequest = { viewModel.toggleConditionDropdown() }
                    ) {
                        listOf("Mint", "Near Mint", "Excellent", "Good", "Fair", "Poor").forEach { condition ->
                            DropdownMenuItem(
                                text = { Text(condition) },
                                onClick = { viewModel.selectCondition(condition) }
                            )
                        }
                    }
                }
            }

            // Listing Type
            item {
                Text(
                    text = "Listing Type",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.listingType == "fixed",
                        onClick = { viewModel.setListingType("fixed") },
                        label = { Text("Fixed Price") }
                    )
                    FilterChip(
                        selected = uiState.listingType == "auction",
                        onClick = { viewModel.setListingType("auction") },
                        label = { Text("Auction") }
                    )
                }
            }

            // Allow Offers (for fixed price)
            if (uiState.listingType == "fixed") {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Allow Offers")
                        Switch(
                            checked = uiState.allowOffers,
                            onCheckedChange = { viewModel.setAllowOffers(it) }
                        )
                    }
                }
            }

            // Shipping Price
            item {
                OutlinedTextField(
                    value = uiState.shippingPrice,
                    onValueChange = { viewModel.updateShippingPrice(it) },
                    label = { Text("Shipping Price (leave empty for free shipping)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    prefix = { Text("$") }
                )
            }

            // Error message
            if (uiState.error != null) {
                item {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Submit button
            item {
                Button(
                    onClick = { viewModel.createListing() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !uiState.isLoading && uiState.isValid
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Create Listing")
                    }
                }
            }

            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
