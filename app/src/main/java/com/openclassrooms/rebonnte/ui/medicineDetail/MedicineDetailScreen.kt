package com.openclassrooms.rebonnte.ui.medicineDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.ErrorScreen
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.components.ConfirmationDialog
import com.openclassrooms.rebonnte.ui.components.TextFieldDialog
import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import com.openclassrooms.rebonnte.ui.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    detailViewModel: MedicineDetailViewModel,
    editViewModel: EditMedicineViewModel,
    onBackClick: () -> Unit,
) {

    val detailState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val formState by editViewModel.formState.collectAsStateWithLifecycle()
    var showNameEditDialog by rememberSaveable { mutableStateOf(false) }
    var showAisleEditDialog by rememberSaveable { mutableStateOf(false) }
    var showStockEditDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(formState.isSuccess, formState.errorId) {
        if (formState.isSuccess) {
            showNameEditDialog = false
            showAisleEditDialog = false
            showStockEditDialog = false
            editViewModel.resetSuccessState()
        }
        formState.errorId?.let { errorResId ->
            detailViewModel.setErrorMessage(errorResId)
            editViewModel.resetSuccessState()
        }
    }

    when (val state = detailState) {
        MedicineDetailState.Loading -> {
            LoadingScreen()
        }

        is MedicineDetailState.MedicineFound -> {
            MedicineDetailContent(
                medicine = state.medicine,
                histories = state.histories,
                onEditNameClick = { showNameEditDialog = true },
                onEditAisleClick = { showAisleEditDialog = true },
                onEditStockClick = { showStockEditDialog = true },
                onBackClick = onBackClick,
                onDeleteClick = { showDeleteConfirmationDialog = true },
            )
            when {
                showNameEditDialog -> {
                    TextFieldDialog(
                        title = stringResource(R.string.edit_name),
                        label = stringResource(R.string.name),
                        initialValue = formState.name,
                        isError = formState.formError.nameBlankError || formState.formError.nameLengthError,
                        errorText = if (formState.formError.nameBlankError) {
                            stringResource(R.string.error_name_empty)
                        } else if (formState.formError.nameLengthError) {
                            stringResource(R.string.error_name_too_long)
                        } else null,
                        onDismiss = { showNameEditDialog = false },
                        onConfirm = { newName ->
                            editViewModel.onSaveName(newName)
                        },
                    )
                }

                showAisleEditDialog -> {
                    TextFieldDialog(
                        title = stringResource(R.string.edit_aisle_number),
                        label = stringResource(R.string.aisle_number),
                        initialValue = formState.aisleNumber,
                        isDigits = true,
                        isError = formState.formError.aisleBlankError || formState.formError.aisleDigitError || formState.formError.aisleDoesNotExistError,
                        errorText = if (formState.formError.aisleBlankError) {
                            stringResource(R.string.error_aisle_empty)
                        } else if (formState.formError.aisleDigitError) {
                            stringResource(R.string.error_aisle_invalid)
                        } else if (formState.formError.aisleDoesNotExistError) {
                            stringResource(R.string.error_aisle_not_exists)
                        } else null,
                        onDismiss = { showAisleEditDialog = false },
                        onConfirm = { newAisle ->
                            editViewModel.onSaveAisle(newAisle)
                        },
                    )
                }

                showStockEditDialog -> {
                    TextFieldDialog(
                        title = stringResource(R.string.edit_stock),
                        label = stringResource(R.string.stock),
                        initialValue = formState.stock,
                        isDigits = true,
                        isError = formState.formError.stockBlankError || formState.formError.stockDigitError,
                        errorText = if (formState.formError.stockBlankError) {
                            stringResource(R.string.error_stock_empty)
                        } else if (formState.formError.stockDigitError) {
                            stringResource(R.string.error_stock_invalid)
                        } else null,
                        onDismiss = { showStockEditDialog = false },
                        onConfirm = { newStock ->
                            editViewModel.onSaveStock(newStock)
                        },
                    )
                }

                showDeleteConfirmationDialog -> {
                    ConfirmationDialog(
                        title = stringResource(R.string.deletion_confirmation),
                        text = stringResource(
                            R.string.please_confirm_the_deletion_of_,
                            state.medicine.name
                        ),
                        onDismissRequest = { showDeleteConfirmationDialog = false },
                        onDismissClick = { showDeleteConfirmationDialog = false },
                        onConfirmClick = {
                            detailViewModel.onArchiveClick()
                            onBackClick()
                        },
                    )
                }
            }
        }

        MedicineDetailState.MedicineNotFound -> {
            ErrorScreen(
                errorMessage = R.string.no_medicine_found,
                isBackEnabled = true,
                onBack = onBackClick,
            )
        }

        is MedicineDetailState.Error -> {
            ErrorScreen(
                errorMessage = state.messageId,
                isBackEnabled = true,
                onBack = onBackClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineDetailContent(
    medicine: MedicineUi,
    histories: List<HistoryUi>,
    onEditNameClick: () -> Unit,
    onEditAisleClick: () -> Unit,
    onEditStockClick: () -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = medicine.name,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete ${medicine.name}"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                item {
                    Text(text = "Details", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailField(
                        titleText = "Name: ",
                        value = medicine.name,
                        contentDescription = "Edit name",
                        onEditClick = onEditNameClick,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailField(
                        titleText = "Localisation: Aisle #",
                        value = medicine.aisleNumber,
                        contentDescription = "Edit aisle number",
                        onEditClick = onEditAisleClick,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailField(
                        titleText = "Current Stock: ",
                        value = medicine.currentStock,
                        contentDescription = "Edit stock",
                        onEditClick = onEditStockClick,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "History", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(histories) { history ->
                    HistoryItem(history = history)
                }
            }
        }
    }
}

@Composable
private fun DetailField(
    titleText: String,
    value: String,
    contentDescription: String,
    onEditClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Surface(
                onClick = onEditClick,
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = contentDescription,
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(history: HistoryUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "User: ${history.username.asString()}")
            Text(text = "Date: ${history.dateTime}")
            Text(text = "Details: ${history.details.asString()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineDetailContentPreview() {
    RebonnteTheme {
        MedicineDetailContent(
            medicine = MedicineUi(
                "1",
                "Paracetamol",
                "1",
                "10"
            ),
            histories = listOf(
                HistoryUi(
                    "1",
                    UiText.RawString("user1"),
                    "2026-08-01 10:00",
                    UiText.RawString("Creation")
                ),
                HistoryUi(
                    "1",
                    UiText.RawString("user2"),
                    "2026-08-02 11:00",
                    UiText.RawString("Stock changed from 10 to 5")
                )
            ),
            onEditNameClick = {},
            onEditAisleClick = {},
            onEditStockClick = {},
            onBackClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MedicineDetailContentDarkPreview() {
    RebonnteTheme {
        MedicineDetailContent(
            medicine = MedicineUi(
                "1",
                "Paracetamol",
                "1",
                "10"
            ),
            histories = listOf(
                HistoryUi(
                    "1",
                    UiText.RawString("user1"),
                    "2026-08-01 10:00",
                    UiText.RawString("Creation")
                ),
                HistoryUi(
                    "1",
                    UiText.RawString("user2"),
                    "2026-08-02 11:00",
                    UiText.RawString("Stock changed from 10 to 5")
                )
            ),
            onEditNameClick = {},
            onEditAisleClick = {},
            onEditStockClick = {},
            onBackClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryItemPreview() {
    RebonnteTheme {
        HistoryItem(
            history = HistoryUi(
                "1",
                UiText.RawString("user1"),
                "2026-08-01 10:00",
                UiText.RawString("Stock changed from 10 to 5")
            )
        )
    }
}
