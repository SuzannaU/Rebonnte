package com.openclassrooms.rebonnte.ui.medicineList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.ErrorScreen
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.MEDICINE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.components.BottomNavigationBar
import com.openclassrooms.rebonnte.ui.components.MedicineItem
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.model.SortOption
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import kotlin.enums.EnumEntries

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


    when (val state = uiState) {
        MedicineListScreenState.Loading -> {
            LoadingScreen()
        }

        is MedicineListScreenState.MedicinesFound -> {
            MedicineListContent(
                medicines = state.medicines,
                sortOptions = SortOption.entries,
                searchQuery = searchQuery,
                selectedSortOption = selectedSortOption,
                onSortOptionClick = { viewModel.onSortOptionSelected(it) },
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                onMedicineClick = onMedicineClick,
                onAislesClick = onAislesClick,
                onAddMedicineClick = onAddMedicineClick,
            )
        }

        is MedicineListScreenState.Error -> {
            ErrorScreen(
                errorMessage = state.message,
                isBackEnabled = true,
                onBack = onAislesClick,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineListContent(
    medicines: List<MedicineUi>,
    sortOptions: EnumEntries<SortOption>,
    searchQuery: String,
    selectedSortOption: SortOption,
    onSortOptionClick: (SortOption) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onMedicineClick: (String) -> Unit,
    onAislesClick: () -> Unit,
    onAddMedicineClick: () -> Unit,
) {
    val screenTitle = stringResource(R.string.medicines)
    Scaffold(
        modifier = Modifier.semantics {
            isTraversalGroup = true
            paneTitle = screenTitle
        },
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier.semantics { traversalIndex = 1f },
                    title = {
                        Text(
                            text = screenTitle,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.semantics { heading() },
                        )
                    },
                    actions = {
                        var expanded by rememberSaveable { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = stringResource(R.string.sorting_options)
                                )
                            }
                            SortDropDown(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                sortOptions = sortOptions,
                                selectedSortOption = selectedSortOption,
                                onSortOptionClick = {
                                    onSortOptionClick(it)
                                    expanded = false
                                },
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            Box(
                Modifier.semantics {
                    isTraversalGroup = true
                    traversalIndex = 2f
                },
            ) {
                BottomNavigationBar(
                    currentRoute = MEDICINE_LIST_ROUTE,
                    onAislesClick = onAislesClick,
                    onMedicinesClick = {},
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { traversalIndex = 3f },
                onClick = onAddMedicineClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_medicine)
                )
            }
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .semantics {
                    isTraversalGroup = true
                    traversalIndex = 4f
                },
        ) {
            CustomSearchBar(
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) },
                onSearch = { onSearchQueryChange(it) },
                placeholder = stringResource(R.string.search),
                modifier = Modifier
                    .semantics { traversalIndex = 1f },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .semantics { traversalIndex = 2f },
                contentAlignment = Alignment.Center
            ) {
                when {
                    searchQuery.isEmpty() && medicines.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.no_medicine_found),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    medicines.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.no_result_found),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(medicines) { medicine ->
                                MedicineItem(
                                    medicine = medicine,
                                    onClick = { onMedicineClick(medicine.id) },
                                    showAisleNumber = true,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SortDropDown(
    expanded: Boolean,
    sortOptions: EnumEntries<SortOption>,
    selectedSortOption: SortOption,
    onSortOptionClick: (SortOption) -> Unit,
    onDismissRequest: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = 0.dp, y = 0.dp)
    ) {
        sortOptions.forEach { sortOption ->
            DropdownMenuItem(
                text = { Text(stringResource(sortOption.labelId)) },
                onClick = { onSortOptionClick(sortOption) },
                leadingIcon = {
                    if (sortOption == selectedSortOption) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(
                                R.string.sortoption_is_selected,
                                sortOption
                            ),
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter),
            windowInsets = WindowInsets(0.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text(text = placeholder) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = {
                                onQueryChange("")
                                keyboardController?.hide()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.clear_search)
                                )
                            }
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },
        ) { }
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
            onMedicineClick = {},
            sortOptions = SortOption.entries,
            searchQuery = "",
            selectedSortOption = SortOption.NAME_ASCENDING,
            onSortOptionClick = {},
            onSearchQueryChange = {},
            onAislesClick = {},
            onAddMedicineClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MedicineListContentNightPreview() {
    RebonnteTheme {
        MedicineListContent(
            medicines = listOf(
                MedicineUi("1", "Paracetamol", "1", "10"),
                MedicineUi("2", "Ibuprofen", "1", "5"),
                MedicineUi("3", "Aspirin", "2", "20")
            ),
            onMedicineClick = {},
            sortOptions = SortOption.entries,
            searchQuery = "",
            selectedSortOption = SortOption.NAME_ASCENDING,
            onSortOptionClick = {},
            onSearchQueryChange = {},
            onAislesClick = {},
            onAddMedicineClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineListContentEmptyPreview() {
    RebonnteTheme {
        MedicineListContent(
            medicines = emptyList(),
            onMedicineClick = {},
            sortOptions = SortOption.entries,
            searchQuery = "",
            selectedSortOption = SortOption.NAME_ASCENDING,
            onSortOptionClick = {},
            onSearchQueryChange = {},
            onAislesClick = {},
            onAddMedicineClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomSearchBarPreview() {
    RebonnteTheme {
        CustomSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            placeholder = "Search",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarActivePreview() {
    RebonnteTheme {
        CustomSearchBar(
            query = "Para",
            onQueryChange = {},
            onSearch = {},
            placeholder = "Search",
        )
    }
}
