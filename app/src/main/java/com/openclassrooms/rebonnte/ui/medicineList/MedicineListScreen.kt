package com.openclassrooms.rebonnte.ui.medicineList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.EmbeddedSearchBar
import com.openclassrooms.rebonnte.ui.MEDICINE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.components.CustomNavigationBar
import com.openclassrooms.rebonnte.ui.model.MedicineUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    viewModel: MedicineListViewModel,
    onMedicineClick: (String) -> Unit,
    onAislesClick: () -> Unit,
) {
    val uiState by viewModel.listScreenState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Medicines",
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    actions = {
                        var expanded by rememberSaveable { mutableStateOf(false) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = null)
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    offset = DpOffset(x = 0.dp, y = 0.dp)
                                ) {
                                    viewModel.sortOptions.forEach { sortOption ->
                                        DropdownMenuItem(
                                            text = { Text(stringResource(sortOption.labelId)) },
                                            onClick = {
                                                viewModel.sortMedicinesBy(sortOption)
                                                expanded = false
                                            },
                                            leadingIcon = {
                                                if (sortOption == selectedSortOption) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                    )
                                                }
                                            }
                                        )
                                    }
//                                DropdownMenuItem(
//                                    onClick = {
//                                        medicineListViewModel.sortByNone()
//                                        expanded = false
//                                    },
//                                    text = { Text("Sort by None") }
//                                )
//                                DropdownMenuItem(
//                                    onClick = {
//                                        medicineListViewModel.sortByName()
//                                        expanded = false
//                                    },
//                                    text = { Text("Sort by Name") }
//                                )
//                                DropdownMenuItem(
//                                    onClick = {
//                                        medicineListViewModel.sortByStock()
//                                        expanded = false
//                                    },
//                                    text = { Text("Sort by Stock") }
//                                )
                                }
                            }
                        }
                    }
                )
                EmbeddedSearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        viewModel.onSearchQueryChange(it)
                    },
                    isSearchActive = isSearchActive,
                    onActiveChanged = { isSearchActive = it }
                )
            }
        },
        bottomBar = {
            CustomNavigationBar(
                currentRoute = MEDICINE_LIST_ROUTE,
                onAislesClick = onAislesClick,
                onMedicinesClick = {},
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.addRandomMedicine() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                MedicineListScreenState.Loading -> {}
                is MedicineListScreenState.MedicinesFound -> {
                    MedicineListContent(
                        medicines = state.medicines,
                        onMedicineClick = onMedicineClick,
                    )
                }

                MedicineListScreenState.NoMedicinesFound -> {}
                MedicineListScreenState.NoResultFound -> {}
            }
        }
    }
}

@Composable
private fun MedicineListContent(
    medicines: List<MedicineUi>,
    onMedicineClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(medicines) { medicine ->
            MedicineItem(
                medicine = medicine,
                onClick = { onMedicineClick(medicine.id) }
            )
        }
    }
}


@Composable
private fun MedicineItem(medicine: MedicineUi, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(medicine.name) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = medicine.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Stock: ${medicine.currentStock}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
    }
}
