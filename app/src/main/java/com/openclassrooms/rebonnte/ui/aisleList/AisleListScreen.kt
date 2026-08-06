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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.components.BottomNavigationBar
import com.openclassrooms.rebonnte.ui.components.TextFieldDialog
import com.openclassrooms.rebonnte.ui.model.AisleUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AisleListScreen(
    viewModel: AisleListViewModel,
    onAisleClick: (String) -> Unit,
    onMedicinesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddAisleDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Aisles",
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
                onClick = { showAddAisleDialog = true }
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
                is AisleListScreenState.AislesFound -> {
                    AisleListContent(
                        aisles = state.aisles,
                        onAisleClick = onAisleClick,
                    )
                }

                is AisleListScreenState.Error -> {}
                AisleListScreenState.Loading -> {}
                AisleListScreenState.NoAisleFound -> {}
            }

            if (showAddAisleDialog) {
                TextFieldDialog(
                    title = "Add Aisle",
                    label = "Aisle Number",
                    initialValue = "",
                    isDigits = true,
                    onDismiss = { showAddAisleDialog = false },
                    onConfirm = {aisleNumber ->
                        viewModel.addAisle(aisleNumber)
                        showAddAisleDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AisleListContent(
    aisles: List<AisleUi>,
    onAisleClick: (String) -> Unit,
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
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
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
            onAisleClick = {}
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