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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.LoadingScreen
import com.openclassrooms.rebonnte.ui.components.TextFieldDialog
import com.openclassrooms.rebonnte.ui.model.HistoryUi
import com.openclassrooms.rebonnte.ui.model.MedicineUi
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    viewModel: MedicineDetailViewModel,
    onBackClick: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showNameEditDialog by rememberSaveable { mutableStateOf(false) }
    var showAisleEditDialog by rememberSaveable { mutableStateOf(false) }
    var showStockEditDialog by rememberSaveable { mutableStateOf(false) }

    when (val state = uiState) {
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
            )
            when {
                showNameEditDialog -> {
                    TextFieldDialog(
                        title = "Edit Name",
                        label = "Name",
                        initialValue = state.medicine.name,
                        onDismiss = { showNameEditDialog = false },
                        onConfirm = { newName ->
                            viewModel.updateName(newName)
                            showNameEditDialog = false
                        },
                    )
                }

                showAisleEditDialog -> {
                    TextFieldDialog(
                        title = "Edit Aisle Number",
                        label = "Aisle Number",
                        initialValue = state.medicine.aisleNumber,
                        isDigits = true,
                        onDismiss = { showAisleEditDialog = false },
                        onConfirm = { newAisle ->
                            viewModel.updateAisle(newAisle)
                            showAisleEditDialog = false
                        },
                    )
                }

                showStockEditDialog -> {
                    TextFieldDialog(
                        title = "Edit Medicine",
                        label = "Stock",
                        initialValue = state.medicine.currentStock,
                        isDigits = true,
                        onDismiss = { showStockEditDialog = false },
                        onConfirm = { newStock ->
                            viewModel.updateStock(newStock)
                            showStockEditDialog = false
                        },
                    )
                }
            }
        }

        MedicineDetailState.MedicineNotFound -> {}
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
                            contentDescription = "navigate back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
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
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(histories) { history ->
                        HistoryItem(history = history)
                    }
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
            Text(text = "User: ${history.userId}")
            Text(text = "Date: ${history.dateTime}")
            Text(text = "Details: ${history.details}")
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
                "Parsjkfjlkjslfkjdlkfjlsdkjflskjacetamol",
                "1",
                "10"
            ),
            histories = listOf(
                HistoryUi("1", "user1", "2026-08-01 10:00"),
                HistoryUi("1", "user2", "2026-08-02 11:00")
            ),
            onEditNameClick = {},
            onEditAisleClick = {},
            onEditStockClick = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryItemPreview() {
    RebonnteTheme {
        HistoryItem(
            history = HistoryUi("1", "user1", "2026-08-01 10:00")
        )
    }
}
