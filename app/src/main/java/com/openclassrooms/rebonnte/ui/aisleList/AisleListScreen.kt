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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.AISLE_LIST_ROUTE
import com.openclassrooms.rebonnte.ui.ErrorScreen
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
    val addAisleFormState by viewModel.addAisleState.collectAsStateWithLifecycle()
    var showAddAisleDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(addAisleFormState.isSuccess) {
        if (addAisleFormState.isSuccess) {
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
                onSignOutClick = { viewModel.onLogout() },
            )
        }

        is AisleListScreenState.Error -> {
            ErrorScreen(
                errorMessage = state.errorMessageId,
                isRetryEnabled = false,
                onRetry = {},
            )
        }

        AisleListScreenState.Loading -> {
            LoadingScreen()
        }

        AisleListScreenState.NoAisleFound -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Text(
                    text = stringResource(R.string.no_aisles_found),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }

    if (showAddAisleDialog) {
        TextFieldDialog(
            title = stringResource(R.string.add_aisle),
            label = stringResource(R.string.aisle_number),
            initialValue = "",
            isDigits = true,
            isError = addAisleFormState.aisleBlankError || addAisleFormState.aisleDigitError || addAisleFormState.aisleExistsError,
            errorText = if (addAisleFormState.aisleBlankError) {
                stringResource(R.string.error_aisle_empty)
            } else if (addAisleFormState.aisleDigitError) {
                stringResource(R.string.error_aisle_invalid)
            } else if (addAisleFormState.aisleExistsError) {
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
    onSignOutClick: () -> Unit,
) {
    val screenTitle = stringResource(R.string.aisles)
    Scaffold(
        modifier = Modifier.semantics {
            isTraversalGroup = true
            paneTitle = screenTitle
        },
        topBar = {
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
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        tonalElevation = 1.dp
                    ) {
                        IconButton(onClick = onSignOutClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = stringResource(R.string.logout)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Box(
                Modifier.semantics {
                    isTraversalGroup = true
                    traversalIndex = 2f
                },
            ) {
                BottomNavigationBar(
                    currentRoute = AISLE_LIST_ROUTE,
                    onAislesClick = {},
                    onMedicinesClick = onMedicinesClick,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics { traversalIndex = 3f },
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
                .semantics {
                    isTraversalGroup = true
                    traversalIndex = 4f
                },
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
            .clickable(
                onClickLabel = stringResource(
                    R.string.navigate_to_aisle_number_n,
                    aisle.number
                )
            ) { onAisleClick() }
            .padding(vertical = 12.dp, horizontal = 32.dp)
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = aisle.number, style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
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
            onSignOutClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AisleListContentDarkPreview() {
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
            onSignOutClick = {}
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