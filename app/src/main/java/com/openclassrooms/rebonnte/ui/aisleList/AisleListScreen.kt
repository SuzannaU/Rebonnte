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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.components.CustomNavigationBar
import com.openclassrooms.rebonnte.ui.model.AisleUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AisleListScreen(
    viewModel: AisleListViewModel,
    onAisleClick: (String) -> Unit,
    onMedicinesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            CustomNavigationBar(
                currentRoute = AISLE_LIST_ROUTE,
                onAislesClick = {},
                onMedicinesClick = onMedicinesClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.addRandomAisle() }
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
                onMedicineClick = { onAisleClick(aisle.id) }
            )
        }
    }
}

@Composable
private fun AisleItem(aisle: AisleUi, onMedicineClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMedicineClick(aisle.number) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = aisle.number.toString(), style = MaterialTheme.typography.bodyMedium)
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
    }
}