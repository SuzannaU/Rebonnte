package com.openclassrooms.rebonnte.ui.medicineList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.MEDICINE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.components.BottomNavigationBar
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    viewModel: MedicineListViewModel,
    onMedicineClick: (String) -> Unit,
    onAislesClick: () -> Unit,
    onAddMedicineClick: () -> Unit,
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
            BottomNavigationBar(
                currentRoute = MEDICINE_LIST_ROUTE,
                onAislesClick = onAislesClick,
                onMedicinesClick = {},
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMedicineClick
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
            .clickable { onClick(medicine.id) }
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

@Composable
private fun EmbeddedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf(query) }
    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }

    val shape: Shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchActive) {
            IconButton(onClick = { activeChanged(false) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        BasicTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                onQueryChange(query)
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )

        if (isSearchActive && searchQuery.isNotEmpty()) {
            IconButton(onClick = {
                searchQuery = ""
                onQueryChange("")
            }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineListContentPreview() {
    RebonnteTheme {
        MedicineListContent(
            medicines = listOf(
                MedicineUi("1", "Paracetamol", "1", "10"),
                MedicineUi("2", "Ibuprofen", "1", "5"),
                MedicineUi("3", "Aspirin", "2", "20")
            ),
            onMedicineClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineItemPreview() {
    RebonnteTheme {
        MedicineItem(
            medicine = MedicineUi("1", "Paracetamol", "1", "10"),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmbeddedSearchBarPreview() {
    RebonnteTheme {
        EmbeddedSearchBar(
            query = "",
            onQueryChange = {},
            isSearchActive = false,
            onActiveChanged = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmbeddedSearchBarActivePreview() {
    RebonnteTheme {
        EmbeddedSearchBar(
            query = "Para",
            onQueryChange = {},
            isSearchActive = true,
            onActiveChanged = {}
        )
    }
}
