package com.openclassrooms.rebonnte.ui.aisleList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.components.BottomNavigationBar
import com.openclassrooms.rebonnte.ui.components.TextFieldDialog
import com.openclassrooms.rebonnte.ui.model.AisleUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun AisleListScreen(
    viewModel: AisleListViewModel,
    onAisleClick: (String) -> Unit,
    onMedicinesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val addAisleState by viewModel.addAisleState.collectAsStateWithLifecycle()
    var showAddAisleDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(addAisleState.isSuccess) {
        if (addAisleState.isSuccess) {
            showAddAisleDialog = false
            viewModel.resetAddAisleState()
        }
    }

    when (val state = uiState) {
        is AisleListScreenState.AislesFound -> {
            AisleListContent(
                aisles = state.aisles,
                onAisleClick = onAisleClick,
                onMedicinesClick = onMedicinesClick,
                onAddAisleClick = {
                    viewModel.resetAddAisleState()
                    showAddAisleDialog = true
                },
            )
        }

        is AisleListScreenState.Error -> {}
        AisleListScreenState.Loading -> {
            LoadingScreen()
        }
        AisleListScreenState.NoAisleFound -> {}
    }

    if (showAddAisleDialog) {
        TextFieldDialog(
            title = stringResource(R.string.add_aisle),
            label = stringResource(R.string.aisle_number),
            initialValue = "",
            isDigits = true,
            isError = addAisleState.aisleBlankError || addAisleState.aisleDigitError || addAisleState.aisleExistsError,
            errorText = if (addAisleState.aisleBlankError) {
                stringResource(R.string.error_aisle_empty)
            } else if (addAisleState.aisleDigitError) {
                stringResource(R.string.error_aisle_invalid)
            } else if (addAisleState.aisleExistsError) {
                stringResource(R.string.error_aisle_exists)
            } else null,
            onDismiss = {
                showAddAisleDialog = false
                viewModel.resetAddAisleState()
            },
            onConfirm = { aisleNumber ->
                viewModel.onAddAisle(aisleNumber)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AisleListContent(
    aisles: List<AisleUi>,
    onAisleClick: (String) -> Unit,
    onMedicinesClick: () -> Unit,
    onAddAisleClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.aisles),
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = AISLE_LIST_ROUTE,
                onAislesClick = {},
                onMedicinesClick = onMedicinesClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAisleClick
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_aisle))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(aisles) { aisle ->
                    AisleItem(
                        aisle = aisle,
                        onAisleClick = { onAisleClick(aisle.number) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AisleItem(aisle: AisleUi, onAisleClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAisleClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = aisle.number, style = MaterialTheme.typography.bodyMedium)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.navigate_to_aisle_number_n, aisle.number),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AisleListContentPreview() {
    RebonnteTheme {
        AisleListContent(
            aisles = listOf(
                AisleUi("1"),
                AisleUi("2"),
                AisleUi("3")
            ),
            onAisleClick = {},
            onMedicinesClick = {},
            onAddAisleClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AisleItemPreview() {
    RebonnteTheme {
        AisleItem(
            aisle = AisleUi("1"),
            onAisleClick = {}
        )
    }
}